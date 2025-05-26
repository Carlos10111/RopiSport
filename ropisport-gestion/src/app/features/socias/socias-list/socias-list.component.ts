import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
// ✅ CORREGIR esta línea (faltaba 'from' y había una coma extra)
import { SociaService } from '../../../core/services/socia/socia.service'; // ← Ajustar la ruta correcta
import { Socia,PaginatedResponse } from '../../../core/models/socia';
@Component({
  selector: 'app-socia-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './socias-list.component.html',
  styleUrls: ['./socias-list.component.scss']
})
export class SociaListComponent implements OnInit, OnDestroy {
  socias: Socia[] = [];
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
  resultadosBusqueda: Socia[] = [];
  private busquedaTerms = new Subject<string>();
  buscando = false;
  
  // Modal y edición
  modalActivo: 'nueva' | 'editar' | 'eliminar' | 'buscarModificar' | 'buscarEliminar' | null = null;
  guardando = false;
  sociaActual: Socia = this.inicializarSocia();
  
  constructor(private sociaService: SociaService) { }
  
  ngOnInit(): void {
    // Configurar búsqueda principal con debounce
    this.subscription.add(
      this.searchTerms.pipe(
        debounceTime(400),
        distinctUntilChanged()
      ).subscribe(term => {
        this.searchText = term;
        this.currentPage = 0;
        this.loadSocias();
      })
    );
    
    // Configurar búsqueda en modal con debounce
    this.subscription.add(
      this.busquedaTerms.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => {
          if (!term.trim()) {
            return of({ content: [] });
          }
          this.buscando = true;
          return this.sociaService.searchSocias(term, 0, 10).pipe(
            catchError(error => {
              this.error = 'Error al buscar socias: ' + error.message;
              return of({ content: [] });
            }),
            finalize(() => this.buscando = false)
          );
        })
      ).subscribe((response: any) => {
        this.resultadosBusqueda = response.content;
      })
    );
    
    // Cargar datos iniciales
    this.loadSocias();
  }
  
  ngOnDestroy(): void {
    // Limpiar suscripciones para prevenir memory leaks
    this.subscription.unsubscribe();
  }
  
  inicializarSocia(): Socia {
    return {
      nombre: '',
      apellidos: '',
      nombreNegocio: '',
      activa: true
    };
  }
  
  loadSocias(): void {
    this.loading = true;
    this.error = '';
    
    // Si hay texto de búsqueda, usamos el endpoint de búsqueda
    const request = this.searchText
      ? this.sociaService.searchSocias(this.searchText, this.currentPage, this.pageSize)
      : this.sociaService.getAllSocias(this.currentPage, this.pageSize);
    
    this.subscription.add(
      request.subscribe({
        next: (response: PaginatedResponse<Socia>) => {
          this.socias = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error al cargar las socias: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
          this.loading = false;
          console.error('Error cargando socias:', err);
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
      this.loadSocias();
    }
  }
  
  // FUNCIONES PARA BÚSQUEDA EN MODAL
  
  buscarSocias(): void {
    this.busquedaTerms.next(this.terminoBusqueda);
  }
  
  // FUNCIONES PARA MODALES
  
  abrirModalNueva(): void {
    this.sociaActual = this.inicializarSocia();
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
  
  seleccionarSociaParaEditar(socia: Socia): void {
    this.sociaActual = { ...socia };
    this.modalActivo = 'editar';
  }
  
  seleccionarSociaParaEliminar(socia: Socia): void {
    this.sociaActual = socia;
    this.modalActivo = 'eliminar';
  }
  
  abrirModalEditar(socia: Socia): void {
    // Para editar directamente desde la tabla
    this.sociaActual = { ...socia };
    this.modalActivo = 'editar';
  }
  
  abrirModalEliminar(socia: Socia): void {
    // Para eliminar directamente desde la tabla
    this.sociaActual = socia;
    this.modalActivo = 'eliminar';
  }
  
  cerrarModal(): void {
    this.modalActivo = null;
    this.guardando = false;
  }
  
  guardarNuevaSocia(): void {
    if (this.guardando) return;
    
    this.guardando = true;
    this.error = '';
    
    this.subscription.add(
      this.sociaService.createSocia(this.sociaActual).subscribe({
        next: (response) => {
          this.loadSocias();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.error = 'Error al crear la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
          console.error('Error creando socia:', err);
        }
      })
    );
  }
  
  guardarEdicionSocia(): void {
    if (this.guardando || !this.sociaActual.id) return;
    
    this.guardando = true;
    this.error = '';
    
    this.subscription.add(
      this.sociaService.updateSocia(this.sociaActual.id, this.sociaActual).subscribe({
        next: (response) => {
          this.loadSocias();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.error = 'Error al actualizar la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
          console.error('Error actualizando socia:', err);
        }
      })
    );
  }
  
  confirmarEliminar(): void {
    if (this.guardando || !this.sociaActual.id) return;
    
    this.guardando = true;
    this.error = '';
    
    this.subscription.add(
      this.sociaService.deleteSocia(this.sociaActual.id).subscribe({
        next: () => {
          this.loadSocias();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.error = 'Error al eliminar la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
          console.error('Error eliminando socia:', err);
        }
      })
    );
  }
}