import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
import { EmpresaService } from '../../../core/services/empresa/empresa.service';
import { SociaService } from '../../../core/services/socia/socia.service';
import { CategoriaService } from '../../../core/services/categoria/categoria.service';
import { Empresa } from '../../../core/models/empresa';
import { EmpresaDTO } from '../../../core/dtos/empresa-dto';
import { PaginatedResponse } from '../../../core/models/paginated-response';
import { Socia } from '../../../core/models/socia';
import { Categoria } from '../../../core/models/categoria';

@Component({
  selector: 'app-empresa-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './empresa-list.component.html',
  styleUrls: ['./empresa-list.component.scss']
})
export class EmpresaComponent implements OnInit, OnDestroy {
  empresas: Empresa[] = [];
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
  resultadosBusqueda: Empresa[] = [];
  private busquedaTerms = new Subject<string>();
  buscando = false;

  // Modal y edición
  modalActivo: 'nuevo' | 'editar' | 'eliminar' | 'buscarModificar' | 'buscarEliminar' | null = null;
  guardando = false;
  empresaActual: Empresa = this.inicializarEmpresa();

  // Datos auxiliares para los selects
  socias: Socia[] = [];
  categorias: Categoria[] = [];

  constructor(
    private empresaService: EmpresaService,
    private sociaService: SociaService,
    private categoriaService: CategoriaService
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
        this.loadEmpresas();
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
          return this.empresaService.searchEmpresas(term).pipe(
            catchError(error => {
              console.error('Error en búsqueda:', error);
              this.error = 'Error al buscar empresas: ' + (error.error?.message || error.message || 'Error desconocido');
              return of({
                content: [],
                page: 0,
                size: 0,
                totalElements: 0,
                totalPages: 0,
                last: true
              } as PaginatedResponse<Empresa>);
            }),
            finalize(() => this.buscando = false)
          );
        })
      ).subscribe((results: PaginatedResponse<Empresa> | never[]) => {
        if (Array.isArray(results)) {
          this.resultadosBusqueda = results;
        } else {
          this.resultadosBusqueda = results.content;
        }
      })
    );

    // Cargar datos iniciales
    this.loadEmpresas();
    this.loadSocias();
    this.loadCategorias();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  inicializarEmpresa(): Empresa {
    return {
      id: 0,
      sociaId: 0,
      nombreNegocio: '',
      descripcionNegocio: '',
      categoriaId: 0,
      nombreCategoria: '',
      direccion: '',
      telefonoNegocio: '',
      emailNegocio: '',
      cif: '',
      epigrafe: '',
      web: '',
      instagram: '',
      facebook: '',
      linkedin: '',
      otrasRedes: '',
      createdAt: null,
      updatedAt: null
    };
  }

  loadEmpresas(): void {
    this.loading = true;
    this.error = '';

    if (this.searchText.trim()) {
      this.subscription.add(
        this.empresaService.busquedaGeneral(this.searchText, undefined, this.currentPage, this.pageSize).subscribe({
          next: (response: PaginatedResponse<Empresa>) => {
            this.empresas = response.content;
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al buscar las empresas', err);
          }
        })
      );
    } else {
      this.subscription.add(
        this.empresaService.getAllEmpresas(this.currentPage, this.pageSize, 'id').subscribe({
          next: (response: PaginatedResponse<Empresa>) => {
            console.log('Respuesta del backend completa:', response);

            if (response && response.content && Array.isArray(response.content)) {
              this.empresas = response.content;
              this.totalElements = response.totalElements || 0;
              this.totalPages = response.totalPages || 0;
            } else {
              console.warn('Respuesta inesperada del backend:', response);
              this.empresas = [];
              this.totalElements = 0;
              this.totalPages = 0;
            }

            console.log('Empresas cargadas:', this.empresas);
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al cargar las empresas', err);
          }
        })
      );
    }
  }

  loadSocias(): void {
    this.subscription.add(
      this.sociaService.getAllSocias(0, 1000).subscribe({
        next: (response) => {
          this.socias = response.content;
        },
        error: (err) => {
          console.error('Error cargando socias:', err);
          if (err.status === 401) {
            this.error = 'Error de autenticación. Por favor, inicia sesión nuevamente.';
          }
        }
      })
    );
  }

  loadCategorias(): void {
    this.subscription.add(
      this.categoriaService.getAllCategorias(0, 1000).subscribe({
        next: (response) => {
          this.categorias = response.content;
        },
        error: (err) => {
          console.error('Error cargando categorías:', err);
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
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadEmpresas();
    }
  }

  // FUNCIONES PARA BÚSQUEDA EN MODAL
  buscarEmpresas(): void {
    this.busquedaTerms.next(this.terminoBusqueda);
  }

  // FUNCIONES PARA MODALES
  abrirModalNuevo(): void {
    this.empresaActual = this.inicializarEmpresa();
    this.modalActivo = 'nuevo';
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

  seleccionarEmpresaParaEditar(empresa: Empresa): void {
    this.empresaActual = { ...empresa };
    this.modalActivo = 'editar';
  }

  seleccionarEmpresaParaEliminar(empresa: Empresa): void {
    this.empresaActual = empresa;
    this.modalActivo = 'eliminar';
  }

  abrirModalEditar(empresa: Empresa): void {
    this.seleccionarEmpresaParaEditar(empresa);
  }

  abrirModalEliminar(empresa: Empresa): void {
    this.empresaActual = empresa;
    this.modalActivo = 'eliminar';
  }

  cerrarModal(): void {
    this.modalActivo = null;
    this.guardando = false;
    this.error = '';
  }

  private convertirEmpresaADTO(empresa: Empresa): EmpresaDTO {
    return {
      sociaId: empresa.sociaId,
      nombreNegocio: empresa.nombreNegocio || '',
      descripcionNegocio: empresa.descripcionNegocio || '',
      categoriaId: empresa.categoriaId || 0,
      direccion: empresa.direccion || '',
      telefonoNegocio: empresa.telefonoNegocio || '',
      emailNegocio: empresa.emailNegocio || '',
      cif: empresa.cif || '',
      epigrafe: empresa.epigrafe || '',
      web: empresa.web || '',
      instagram: empresa.instagram || '',
      facebook: empresa.facebook || '',
      linkedin: empresa.linkedin || '',
      otrasRedes: empresa.otrasRedes || ''
    };
  }

  guardarNuevaEmpresa(): void {
    if (this.guardando) return;

    this.guardando = true;
    this.error = '';

    const empresaDTO = this.convertirEmpresaADTO(this.empresaActual);

    this.subscription.add(
      this.empresaService.createEmpresa(empresaDTO).subscribe({
        next: (response) => {
          this.loadEmpresas();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al crear la empresa', err);
        }
      })
    );
  }

  guardarEdicionEmpresa(): void {
    if (this.guardando || !this.empresaActual.id) return;

    this.guardando = true;
    this.error = '';

    const empresaDTO = this.convertirEmpresaADTO(this.empresaActual);

    this.subscription.add(
      this.empresaService.updateEmpresa(this.empresaActual.id, empresaDTO).subscribe({
        next: (response) => {
          this.loadEmpresas();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al actualizar la empresa', err);
        }
      })
    );
  }

  confirmarEliminar(): void {
    if (this.guardando || !this.empresaActual.id) return;

    this.guardando = true;
    this.error = '';

    this.subscription.add(
      this.empresaService.deleteEmpresa(this.empresaActual.id).subscribe({
        next: () => {
          this.loadEmpresas();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al eliminar la empresa', err);
        }
      })
    );
  }

  // Función auxiliar para obtener el nombre de la socia
  getNombreSocia(sociaId: number): string {
    const socia = this.socias.find(s => s.id === sociaId);
    return socia ? `${socia.nombre} ${socia.apellidos}` : '';
  }

  // Función auxiliar para obtener el nombre de la categoría
  getNombreCategoria(categoriaId: number): string {
    const categoria = this.categorias.find(c => c.id === categoriaId);
    return categoria ? categoria.nombre : '';
  }
}

