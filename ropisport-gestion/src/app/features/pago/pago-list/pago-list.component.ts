import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
import { PagoService } from '../../../core/services/pago/pago.service';
import { SociaService } from '../../../core/services/socia/socia.service';
import { Pago } from '../../../core/models/pago';
import { PagoDTO } from '../../../core/dtos/pago-dto';
import { PagoDetalle } from '../../../core/models/pago-detalle';
import { PagoDetalleDTO } from '../../../core/dtos/pago-detalle-dto';
import { PaginatedResponse } from '../../../core/models/paginated-response';
import { Socia } from '../../../core/models/socia';
import { MetodoPago } from '../../../core/enums/metodo-pago';

@Component({
  selector: 'app-pago-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './pago-list.component.html',
  styleUrls: ['./pago-list.component.scss']
})
export class PagoListComponent implements OnInit, OnDestroy {
  pagos: Pago[] = [];
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
  resultadosBusqueda: Pago[] = [];
  private busquedaTerms = new Subject<string>();
  buscando = false;
  
  // Modal y edición
  modalActivo: 'nuevo' | 'editar' | 'eliminar' | 'buscarModificar' | 'buscarEliminar' | 'verDetalles' | null = null;
  guardando = false;
  pagoActual: Pago = this.inicializarPago();
  
  // Socias para el select
  socias: Socia[] = [];
  
  // Enum para template
  metodosPago = Object.values(MetodoPago);
  
  constructor(
    private pagoService: PagoService,
    private sociaService: SociaService
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
        this.loadPagos();
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
          return this.pagoService.searchPagos(term).pipe(
            catchError(error => {
              console.error('Error en búsqueda:', error);
              this.error = 'Error al buscar pagos: ' + (error.error?.message || error.message || 'Error desconocido');
              return of([]);
            }),
            finalize(() => this.buscando = false)
          );
        })
      ).subscribe((results: Pago[]) => {
        this.resultadosBusqueda = results;
      })
    );
    
    // Cargar datos iniciales
    this.loadPagos();
    this.loadSocias();
  }
  
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
  
  inicializarPago(): Pago {
    return {
      id: 0,
      sociaId: 0,
      nombreSocia: '',
      monto: 0,
      fechaPago: '',
      concepto: '',
      metodoPago: MetodoPago.EFECTIVO,
      confirmado: false,
      detalles: [],
      createdAt: '',
      updatedAt: ''
    };
  }
  
  inicializarDetalle(): PagoDetalle {
    return {
      id: 0,
      pagoId: 0,
      concepto: '',
      monto: 0,
      fechaDetalle: '',
      notas: '',
      createdAt: '',
      updatedAt: ''
    };
  }
  
  loadPagos(): void {
    this.loading = true;
    this.error = '';
    
    if (this.searchText.trim()) {
      // Modo búsqueda
      this.subscription.add(
        this.pagoService.searchPagos(this.searchText).subscribe({
          next: (results: Pago[]) => {
            console.log('Respuesta de búsqueda:', results);
            this.pagos = this.procesarPagos(results);
            this.totalElements = results.length;
            this.totalPages = 1;
            this.currentPage = 0;
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al buscar los pagos', err);
          }
        })
      );
    } else {
      // Modo paginado normal
      this.subscription.add(
        this.pagoService.getAllPagos(this.currentPage, this.pageSize, 'fechaPago').subscribe({
          next: (response: PaginatedResponse<Pago>) => {
            console.log('Respuesta del backend completa:', response);
            
            // CORRECCIÓN PROBLEMA 1: Procesar correctamente la respuesta paginada
            if (response && response.content && Array.isArray(response.content)) {
              this.pagos = this.procesarPagos(response.content);
              this.totalElements = response.totalElements || 0;
              this.totalPages = response.totalPages || 0;
            } else {
              console.warn('Respuesta inesperada del backend:', response);
              this.pagos = [];
              this.totalElements = 0;
              this.totalPages = 0;
            }
            
            console.log('Pagos procesados:', this.pagos);
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al cargar los pagos', err);
          }
        })
      );
    }
  }

  // CORRECCIÓN PROBLEMA 3: Función para procesar fechas del backend
  private procesarPagos(pagos: Pago[]): Pago[] {
    return pagos.map(pago => ({
      ...pago,
      fechaPago: this.procesarFechaDelBackend(pago.fechaPago),
      detalles: pago.detalles ? pago.detalles.map(detalle => ({
        ...detalle,
        fechaDetalle: this.procesarFechaDelBackend(detalle.fechaDetalle)
      })) : []
    }));
  }

  // CORRECCIÓN PROBLEMA 3: Procesar fecha que viene del backend
  private procesarFechaDelBackend(fecha: string): string {
    if (!fecha) return '';
    
    try {
      // Si viene como array [2025,5,30,23,27], convertirlo
      if (fecha.includes(',')) {
        const parts = fecha.replace(/[\[\]]/g, '').split(',').map(n => parseInt(n.trim()));
        if (parts.length >= 5) {
          // Los meses en JavaScript van de 0-11, pero LocalDateTime va de 1-12
          const date = new Date(parts[0], parts[1] - 1, parts[2], parts[3], parts[4], parts[5] || 0);
          return date.toISOString();
        }
      }
      
      // Si es una fecha ISO válida, devolverla como está
      if (fecha.includes('T') || fecha.includes('-')) {
        return new Date(fecha).toISOString();
      }
      
      return fecha;
    } catch (error) {
      console.error('Error procesando fecha:', fecha, error);
      return '';
    }
  }

  // CORRECCIÓN PROBLEMA 3: Convertir fecha para enviar al backend (formato LocalDateTime)
  private convertirFechaParaBackend(fechaHtml: string): string {
    if (!fechaHtml) return '';
    
    try {
      const date = new Date(fechaHtml);
      // Formato: YYYY-MM-DDTHH:MM:SS (sin la Z del final para LocalDateTime)
      return date.toISOString().slice(0, 19);
    } catch (error) {
      console.error('Error convirtiendo fecha para backend:', fechaHtml, error);
      return fechaHtml;
    }
  }

  // CORRECCIÓN PROBLEMA 3: Convertir fecha del backend para el input datetime-local
  private convertirFechaParaInput(fecha: string): string {
    if (!fecha) return '';
    
    try {
      const date = new Date(fecha);
      // Formato para datetime-local: YYYY-MM-DDTHH:MM
      return date.toISOString().slice(0, 16);
    } catch (error) {
      console.error('Error convirtiendo fecha para input:', fecha, error);
      return '';
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
      this.loadPagos();
    }
  }
  
  // FUNCIONES PARA BÚSQUEDA EN MODAL
  buscarPagos(): void {
    this.busquedaTerms.next(this.terminoBusqueda);
  }
  
  // FUNCIONES PARA MODALES
  abrirModalNuevo(): void {
    this.pagoActual = this.inicializarPago();
    // Establecer fecha actual por defecto en formato datetime-local
    const now = new Date();
    this.pagoActual.fechaPago = now.toISOString().slice(0, 16);
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
  
  seleccionarPagoParaEditar(pago: Pago): void {
    this.pagoActual = { 
      ...pago,
      detalles: pago.detalles ? [...pago.detalles] : []
    };
    
    // CORRECCIÓN PROBLEMA 3: Convertir fecha para el input
    this.pagoActual.fechaPago = this.convertirFechaParaInput(pago.fechaPago);
    
    // Convertir fechas de detalles también
    if (this.pagoActual.detalles) {
      this.pagoActual.detalles = this.pagoActual.detalles.map(detalle => ({
        ...detalle,
        fechaDetalle: this.convertirFechaParaInput(detalle.fechaDetalle)
      }));
    }
    
    this.modalActivo = 'editar';
  }
  
  seleccionarPagoParaEliminar(pago: Pago): void {
    this.pagoActual = pago;
    this.modalActivo = 'eliminar';
  }
  
  abrirModalEditar(pago: Pago): void {
    this.seleccionarPagoParaEditar(pago);
  }
  
  abrirModalEliminar(pago: Pago): void {
    this.pagoActual = pago;
    this.modalActivo = 'eliminar';
  }
  
  verDetalles(pago: Pago): void {
    this.pagoActual = pago;
    this.modalActivo = 'verDetalles';
  }
  
  cerrarModal(): void {
    this.modalActivo = null;
    this.guardando = false;
  }
  
  // FUNCIONES PARA MANEJAR DETALLES
  agregarDetalle(): void {
    if (!this.pagoActual.detalles) {
      this.pagoActual.detalles = [];
    }
    const nuevoDetalle = this.inicializarDetalle();
    // Establecer fecha actual por defecto
    const now = new Date();
    nuevoDetalle.fechaDetalle = now.toISOString().slice(0, 16);
    this.pagoActual.detalles.push(nuevoDetalle);
  }
  
  eliminarDetalle(index: number): void {
    if (this.pagoActual.detalles && index >= 0 && index < this.pagoActual.detalles.length) {
      this.pagoActual.detalles.splice(index, 1);
    }
  }
  
  private convertirPagoADTO(pago: Pago): PagoDTO {
    const detallesDTO: PagoDetalleDTO[] = pago.detalles ? pago.detalles.map(detalle => ({
      pagoId: pago.id,
      concepto: detalle.concepto || '',
      monto: detalle.monto || 0,
      fechaDetalle: this.convertirFechaParaBackend(detalle.fechaDetalle), // CORRECCIÓN PROBLEMA 3
      notas: detalle.notas || ''
    })) : [];
    
    return {
      sociaId: pago.sociaId,
      monto: pago.monto,
      fechaPago: this.convertirFechaParaBackend(pago.fechaPago), // CORRECCIÓN PROBLEMA 3
      concepto: pago.concepto || '',
      metodoPago: pago.metodoPago.toString(),
      confirmado: pago.confirmado,
      detalles: detallesDTO
    };
  }
  
  guardarNuevoPago(): void {
    if (this.guardando) return;
    
    this.guardando = true;
    this.error = '';
    
    const pagoDTO = this.convertirPagoADTO(this.pagoActual);
    
    this.subscription.add(
      this.pagoService.createPago(pagoDTO).subscribe({
        next: (response) => {
          this.loadPagos();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al crear el pago', err);
        }
      })
    );
  }
  
  guardarEdicionPago(): void {
    if (this.guardando || !this.pagoActual.id) return;
    
    this.guardando = true;
    this.error = '';
    
    const pagoDTO = this.convertirPagoADTO(this.pagoActual);
    
    this.subscription.add(
      this.pagoService.updatePago(this.pagoActual.id, pagoDTO).subscribe({
        next: (response) => {
          this.loadPagos();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al actualizar el pago', err);
        }
      })
    );
  }
  
  confirmarEliminar(): void {
    if (this.guardando || !this.pagoActual.id) return;
    
    this.guardando = true;
    this.error = '';
    
    this.subscription.add(
      this.pagoService.deletePago(this.pagoActual.id).subscribe({
        next: () => {
          this.loadPagos();
          this.cerrarModal();
          this.guardando = false;
        },
        error: (err) => {
          this.guardando = false;
          this.handleError('Error al eliminar el pago', err);
        }
      })
    );
  }
  
  // Función auxiliar para obtener el nombre de la socia
  getNombreSocia(sociaId: number): string {
    const socia = this.socias.find(s => s.id === sociaId);
    return socia ? `${socia.nombre} ${socia.apellidos}` : '';
  }
  
  // Función auxiliar para formatear método de pago
  formatearMetodoPago(metodo: string): string {
    switch (metodo) {
      case 'EFECTIVO': return 'Efectivo';
      case 'TRANSFERENCIA': return 'Transferencia';
      case 'TARJETA': return 'Tarjeta';
      case 'DOMICILIACION': return 'Domiciliación';
      default: return metodo;
    }
  }
}