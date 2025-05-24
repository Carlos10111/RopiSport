import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
import { InstitucionService } from '../../../core/services/institucion/institucion.service';
import { TipoInstitucionService } from '../../../core/services/tipo-institucion/tipo-institucion.service';
import { Institucion } from '../../../core/models/institucion';
import { InstitucionDTO } from '../../../core/dtos/institucion-dto';
import { PaginatedResponse } from '../../../core/dtos/paginated-response';
import { TipoInstitucion } from '../../../core/models/tipo-institucion';

@Component({
  selector: 'app-institucion-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './institucion-list.component.html',
  styleUrls: ['./institucion-list.component.scss']
})
export class InstitucionListComponent implements OnInit, OnDestroy {
  instituciones: Institucion[] = [];
  loading = false;
  error = '';
  
  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  
  // Búsqueda principal
  searchText = '';
  private searchTerms = new Subject<string>();
  private subscription: Subscription = new Subscription();
  
  // Búsqueda en modal
  terminoBusqueda = '';
  resultadosBusqueda: Institucion[] = [];
  private busquedaTerms = new Subject<string>();
  buscando = false;
  
  // Modal y edición
  modalActivo: 'nueva' | 'editar' | 'eliminar' | 'buscarModificar' | 'buscarEliminar' | null = null;
  guardando = false;
  institucionActual: Institucion = this.inicializarInstitucion();
  
  // Tipos de institución para el select
  tiposInstitucion: TipoInstitucion[] = [];
  
  constructor(
    private institucionService: InstitucionService,
    private tipoInstitucionService: TipoInstitucionService
  ) { }
  
  ngOnInit(): void {
    // Configurar búsqueda principal con debounce
    this.subscription.add(
      this.searchTerms.pipe(
        debounceTime(400),
        distinctUntilChanged()
      ).subscribe(term => {
        this.searchText = term;
        this.currentPage = 0;
        this.loadInstituciones();
      })
    );
    
    // Configurar búsqueda en modal con debounce
    this.subscription.add(
      this.busquedaTerms.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => {
          if (!term.trim()) {
            return of([]);
          }
          this.buscando = true;
          return this.institucionService.searchInstituciones(term).pipe(
            catchError(error => {
              console.error('Error en búsqueda:', error);
              this.error = 'Error al buscar instituciones: ' + (error.error?.message || error.message || 'Error desconocido');
              return of([]);
            }),
            finalize(() => this.buscando = false)
          );
        })
      ).subscribe((results: Institucion[]) => {
        this.resultadosBusqueda = results;
      })
    );
    
    // Cargar datos iniciales
    this.loadInstituciones();
    this.loadTiposInstitucion();
  }
  
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
  
  inicializarInstitucion(): Institucion {
    return {
      id: 0,
      nombreInstitucion: '',
      personaContacto: '',
      cargo: '',
      telefono: '',
      email: '',
      web: '',
      tipoInstitucionId: 0,
      nombreTipoInstitucion: '',
      observaciones: '',
      createdAt: '',
      updatedAt: ''
    };
  }
  
  loadInstituciones(): void {
    this.loading = true;
    this.error = '';
    
    if (this.searchText.trim()) {
      // Modo búsqueda
      this.subscription.add(
        this.institucionService.searchInstituciones(this.searchText).subscribe({
          next: (results: Institucion[]) => {
            console.log('Respuesta de búsqueda:', results);
            this.instituciones = results;
            this.totalElements = results.length;
            this.totalPages = 1;
            this.currentPage = 0;
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al buscar las instituciones', err);
          }
        })
      );
    } else {
      // Modo paginado normal
      this.subscription.add(
        this.institucionService.getAllInstituciones(this.currentPage, this.pageSize, 'id').subscribe({
          next: (response: PaginatedResponse<Institucion>) => {
            console.log('Respuesta del backend completa:', response);
            this.instituciones = response.content;
            console.log('Instituciones cargadas:', this.instituciones);
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al cargar las instituciones', err);
          }
        })
      );
    }
  }
  
  loadTiposInstitucion(): void {
    this.subscription.add(
      this.tipoInstitucionService.getAllTiposInstitucion().subscribe({
        next: (tipos) => {
          this.tiposInstitucion = tipos;
        },
        error: (err) => {
          console.error('Error cargando tipos de institución:', err);
          // Si es error 401, podría ser problema de autenticación general
          if (err.status === 401) {
            this.error = 'Error de autenticación. Por favor, inicia sesión nuevamente.';
          }
        }
      })
    );
  }
  
  private handleError(message: string, err: any): void {
    this.loading = false;
    
    if (err.status === 401) {
      this.error = 'Error de autenticación. Por favor, verifica que hayas iniciado sesión correctamente.';
    } else if (err.status === 403) {
      this.error = 'No tienes permisos para realizar esta acción.';
    } else if (err.status === 0) {
      this.error = 'No se puede conectar con el servidor. Verifica tu conexión.';
    } else {
      this.error = message + ': ' + (err.error?.message || err.message || 'Ocurrió un problema en el servidor');
    }
    
    console.error(message + ':', err);
  }
  
  onSearch(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchTerms.next(target.value);
  }
  
  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages && !this.searchText.trim()) {
      this.currentPage = page;
      this.loadInstituciones();
    }
  }
  
  // FUNCIONES PARA BÚSQUEDA EN MODAL
  buscarInstituciones(): void {
    this.busquedaTerms.next(this.terminoBusqueda);
  }
  
  // FUNCIONES PARA MODALES
  abrirModalNueva(): void {
    this.institucionActual = this.inicializarInstitucion();
    this.modalActivo = 'nueva';
  }
  
  abrirModalBuscarParaModificar(): void {
    this.terminoBusqueda = '';
    this.resultadosBusqueda = [];
    this.modalActivo = 'buscarModificar';
  }
  
  abrirModalBuscarParaEliminar(): void {
    this.terminoBusqueda = '';
    this.resultadosBusqueda = [];
    this.modalActivo = 'buscarEliminar';
  }
  
  seleccionarInstitucionParaEditar(institucion: Institucion): void {
    this.institucionActual = { ...institucion };
    this.modalActivo = 'editar';
  }
  
  seleccionarInstitucionParaEliminar(institucion: Institucion): void {
    this.institucionActual = institucion;
    this.modalActivo = 'eliminar';
  }
  
  abrirModalEditar(institucion: Institucion): void {
    this.institucionActual = { ...institucion };
    this.modalActivo = 'editar';
  }
  
  abrirModalEliminar(institucion: Institucion): void {
    this.institucionActual = institucion;
    this.modalActivo = 'eliminar';
  }
  
  cerrarModal(): void {
    this.modalActivo = null;
    this.guardando = false;
  }
  
  guardarNuevaInstitucion(): void {
    if (this.guardando) return;
    
    this.guardando = true;
    this.error = '';
    
    const institucionDTO: InstitucionDTO = {
      nombreInstitucion: this.institucionActual.nombreInstitucion,
      personaContacto: this.institucionActual.personaContacto,
      cargo: this.institucionActual.cargo,
      telefono: this.institucionActual.telefono,
      email: this.institucionActual.email,
      web: this.institucionActual.web,
      tipoInstitucionId: this.institucionActual.tipoInstitucionId,
      observaciones: this.institucionActual.observaciones
    };
    
    this.subscription.add(
      this.institucionService.createInstitucion(institucionDTO).subscribe({
        next: (response) => {
          this.loadInstituciones();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al crear la institución', err);
        }
      })
    );
  }
  
  guardarEdicionInstitucion(): void {
    if (this.guardando || !this.institucionActual.id) return;
    
    this.guardando = true;
    this.error = '';
    
    const institucionDTO: InstitucionDTO = {
      nombreInstitucion: this.institucionActual.nombreInstitucion,
      personaContacto: this.institucionActual.personaContacto,
      cargo: this.institucionActual.cargo,
      telefono: this.institucionActual.telefono,
      email: this.institucionActual.email,
      web: this.institucionActual.web,
      tipoInstitucionId: this.institucionActual.tipoInstitucionId,
      observaciones: this.institucionActual.observaciones
    };
    
    this.subscription.add(
      this.institucionService.updateInstitucion(this.institucionActual.id, institucionDTO).subscribe({
        next: (response) => {
          this.loadInstituciones();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al actualizar la institución', err);
        }
      })
    );
  }
  
  confirmarEliminar(): void {
    if (this.guardando || !this.institucionActual.id) return;
    
    this.guardando = true;
    this.error = '';
    
    this.subscription.add(
      this.institucionService.deleteInstitucion(this.institucionActual.id).subscribe({
        next: () => {
          this.loadInstituciones();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al eliminar la institución', err);
        }
      })
    );
  }
}