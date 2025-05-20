import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
import { InstitucionService } from '../../../core/services/institucion/institucion.service';
import { Institucion } from '../../../core/models/institucion';
import { PaginatedResponse } from '../../../core/models/paginated-response';
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
  tiposInstitucion: TipoInstitucion[] = [];

  loading = false;
  error = '';

  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  searchText = '';
  private searchTerms = new Subject<string>();
  private subscription: Subscription = new Subscription();

  terminoBusqueda = '';
  resultadosBusqueda: Institucion[] = [];
  private busquedaTerms = new Subject<string>();
  buscando = false;

  modalActivo: 'editar' | 'eliminar' | 'buscarModificar' | 'buscarEliminar' | 'nueva' | null = null;
  guardando = false;
  institucionActual: Institucion = this.inicializarInstitucion();

  constructor(private institucionService: InstitucionService) { }

  ngOnInit(): void {
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

    this.subscription.add(
      this.busquedaTerms.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => {
          if (!term.trim()) return of({ content: [] });
          this.buscando = true;
          return this.institucionService.searchInstituciones(term, 0, 10).pipe(
            catchError(error => {
              this.error = 'Error al buscar instituciones: ' + error.message;
              return of({ content: [] });
            }),
            finalize(() => this.buscando = false)
          );
        })
      ).subscribe((response: any) => {
        this.resultadosBusqueda = response.content;
      })
    );

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
    const request = this.searchText
      ? this.institucionService.searchInstituciones(this.searchText, this.currentPage, this.pageSize)
      : this.institucionService.getAllInstituciones(this.currentPage, this.pageSize);

    this.subscription.add(
      request.subscribe({
        next: (response: PaginatedResponse<Institucion>) => {
          this.instituciones = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error al cargar instituciones: ' + (err.error?.message || 'Problema en el servidor');
          this.loading = false;
          console.error('Error cargando instituciones:', err);
        }
      })
    );
  }

  loadTiposInstitucion(): void {
    this.subscription.add(
      this.institucionService.getTiposInstitucion().subscribe({
        next: (tipos: TipoInstitucion[]) => {
          this.tiposInstitucion = tipos;
        },
        error: (err) => {
          this.error = 'Error al cargar tipos de institución: ' + (err.error?.message || 'Problema en el servidor');
          console.error('Error cargando tipos de institución:', err);
        }
      })
    );
  }

  onSearch(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchTerms.next(target.value);
  }

  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadInstituciones();
    }
  }

  buscarInstituciones(): void {
    this.busquedaTerms.next(this.terminoBusqueda);
  }

  abrirModalNueva(): void {
    this.institucionActual = this.inicializarInstitucion();
    this.modalActivo = 'nueva';
  }

  abrirModalEditar(institucion: Institucion): void {
    this.institucionActual = { ...institucion };
    this.modalActivo = 'editar';
  }

  abrirModalEliminar(institucion: Institucion): void {
    this.institucionActual = institucion;
    this.modalActivo = 'eliminar';
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

  cerrarModal(): void {
    this.modalActivo = null;
    this.guardando = false;
  }

  guardarNuevaInstitucion(): void {
    if (this.guardando || !this.institucionActual.nombreInstitucion) return;
    this.guardando = true;
    this.error = '';

    this.subscription.add(
      this.institucionService.createInstitucion(this.institucionActual).subscribe({
        next: () => {
          this.loadInstituciones();
          this.cerrarModal();
          this.institucionActual = this.inicializarInstitucion();
        },
        error: (err) => {
          this.guardando = false;
          this.error = 'Error al crear institución: ' + (err.error?.message || 'Problema en el servidor');
          console.error('Error creando institución:', err);
        }
      })
    );
  }

  guardarEdicionInstitucion(): void {
    if (this.guardando || !this.institucionActual.id) return;
    this.guardando = true;
    this.error = '';
    this.subscription.add(
      this.institucionService.updateInstitucion(this.institucionActual.id, this.institucionActual).subscribe({
        next: () => {
          this.loadInstituciones();
          this.cerrarModal();
        },
        error: (err) => {
          this.guardando = false;
          this.error = 'Error al actualizar: ' + (err.error?.message || 'Problema en el servidor');
          console.error('Error actualizando institución:', err);
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
        },
        error: (err) => {
          this.guardando = false;
          this.error = 'Error al eliminar: ' + (err.error?.message || 'Problema en el servidor');
          console.error('Error eliminando institución:', err);
        }
      })
    );
  }
}