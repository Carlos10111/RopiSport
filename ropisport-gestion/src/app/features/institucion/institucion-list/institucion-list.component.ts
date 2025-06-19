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
import { PaginatedResponse } from '../../../core/models/paginated-response';
import { TipoInstitucion } from '../../../core/models/tipo-institucion';
import { TipoInstitucionDTO } from '../../../core/dtos/tipo-institucion-dto';
import * as XLSX from 'xlsx';

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
  
  mostrarFormularioTipo = false; mostrarEliminarTipo = false;
  nuevoTipo: TipoInstitucionDTO = { nombre: '', descripcion: '' };
  tipoAEliminarId: number | null = null;

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
      ).subscribe((results: never[] | PaginatedResponse<Institucion>) => {
        if (Array.isArray(results)) {
          // Puede ser un array vacío
          this.resultadosBusqueda = results;
        } else {
          // Es un objeto paginado
          this.resultadosBusqueda = results.content;
          this.totalElements = results.totalElements;
          this.totalPages = results.totalPages;
        }
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
        this.institucionService.searchInstituciones(this.searchText, undefined, this.currentPage, this.pageSize, 'id,desc')
  .subscribe({
    next: (response) => {
      this.instituciones = response.content;
      this.totalElements = response.totalElements;
      this.totalPages = response.totalPages;
      this.currentPage = response.page;
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
  buscarInstituciones() {
    const term = this.terminoBusqueda?.toLowerCase().trim();
  
    if (!term) {
      this.resultadosBusqueda = [];
      return;
    }
  
    this.resultadosBusqueda = this.instituciones.filter(inst => {
      const nombre = inst.nombreInstitucion?.toLowerCase() || '';
      const contacto = inst.personaContacto?.toLowerCase() || '';
      const email = inst.email?.toLowerCase() || '';
      const telefono = inst.telefono || '';
      const cargo = inst.cargo?.toLowerCase() || '';
      const tipoNombre = inst.nombreTipoInstitucion?.toLowerCase() || '';
  
      return (
        nombre.includes(term) ||
        contacto.includes(term) ||
        email.includes(term) ||
        telefono.includes(term) ||
        cargo.includes(term) ||
        tipoNombre.includes(term)
      );
    });
  
    console.log('Resultados encontrados:', this.resultadosBusqueda);
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
    //this.buscarInstituciones();
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

  crearNuevoTipoInstitucion(): void {
    if (!this.nuevoTipo.nombre.trim()) return;
  
    this.tipoInstitucionService.createTipoInstitucion(this.nuevoTipo).subscribe({
      next: (tipoCreado) => {
        this.tiposInstitucion.push(tipoCreado);
        this.institucionActual.tipoInstitucionId = tipoCreado.id;
        this.nuevoTipo = { nombre: '', descripcion: '' };
        this.mostrarFormularioTipo = false;
        this.loadTiposInstitucion(); 
      },
      error: (err) => {
        this.handleError('Error al crear tipo de institución', err);
      }
    });
  }

  eliminarTipoInstitucion() {
    if (!this.tipoAEliminarId) return;
  
    this.tipoInstitucionService.deleteTipoInstitucion(this.tipoAEliminarId).subscribe({
      next: () => {
        this.tiposInstitucion = this.tiposInstitucion.filter(t => t.id !== this.tipoAEliminarId);
        this.tipoAEliminarId = null;
        this.mostrarEliminarTipo = false;
        this.loadTiposInstitucion(); 
      },
      error: (err) => {
        this.handleError('Error al eliminar tipo de institución', err);
      }
    });
  }
  
    // Exportar tabla a Excel
    exportarExcel(): void {
      if (this.instituciones.length === 0) {
        alert('No hay datos para exportar');
        return;
      }
  
      // Preparar los datos para Excel
      const datosParaExcel = this.instituciones.map(institucion => ({
        'ID': institucion.id || '',
        'Nombre Institución': institucion.nombreInstitucion || '',
        'Persona Contacto': institucion.personaContacto || '',
        'Cargo': institucion.cargo || '',
        'Teléfono': institucion.telefono || '',
        'Email': institucion.email || '',
        'Web': institucion.web || '',
        'Tipo Institución': institucion.nombreTipoInstitucion || '',
        'Observaciones': institucion.observaciones || '',
        'Fecha Creación': this.getFechaFormateada(institucion.createdAt),
        'Fecha Actualización': this.getFechaFormateada(institucion.updatedAt)
      }));
  
      // Crear workbook y worksheet
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(datosParaExcel);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      
      // Agregar worksheet al workbook
      XLSX.utils.book_append_sheet(wb, ws, 'Instituciones');
      
      // Generar nombre del archivo con fecha actual
      const fechaActual = new Date().toLocaleDateString('es-ES').replace(/\//g, '-');
      const nombreArchivo = `instituciones_${fechaActual}.xlsx`;
      
      // Descargar archivo
      XLSX.writeFile(wb, nombreArchivo);
    }
  
    // Método auxiliar para formatear fechas
    private getFechaFormateada(fecha: string | undefined): string {
      if (!fecha) return '';
      
      try {
        const fechaObj = new Date(fecha);
        return fechaObj.toLocaleDateString('es-ES');
      } catch (error) {
        return '';
      }
    }

}