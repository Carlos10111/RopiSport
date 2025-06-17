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
              return of({
                content: [],
                page: 0,
                size: 0,
                totalElements: 0,
                totalPages: 0,
                last: true
              } as PaginatedResponse<Pago>);
            }),
            finalize(() => this.buscando = false)
          );
        })
      ).subscribe((results: PaginatedResponse<Pago> | never[]) => {
        if (Array.isArray(results)) {
          this.resultadosBusqueda = results;
        } else {
          this.resultadosBusqueda = this.procesarPagos(results.content);
        }
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
      fechaPago: new Date(),
      concepto: '',
      metodoPago: MetodoPago.EFECTIVO,
      confirmado: false,
      detalles: [],
      createdAt: new Date(),
      updatedAt: new Date()
    };
  }
  
  inicializarDetalle(): PagoDetalle {
    return {
      id: 0,
      pagoId: 0,
      concepto: '',
      monto: 0,
      fechaDetalle: new Date(),
      notas: '',
      createdAt: new Date(),
      updatedAt: new Date()
    };
  }

  /**
   * Convierte LocalDateTime del backend a Date
   */
  private parseLocalDateTime(dateValue: any): Date {
    if (!dateValue) return new Date();
    
    // Si ya es una fecha válida
    if (dateValue instanceof Date) return dateValue;
    
    // Si es string ISO
    if (typeof dateValue === 'string' && (dateValue.includes('T') || dateValue.includes('-'))) {
      return new Date(dateValue);
    }
    
    // Si viene como array [year, month, day, hour, minute, second, nano]
    if (Array.isArray(dateValue) && dateValue.length >= 3) {
      const [year, month, day, hour = 0, minute = 0, second = 0] = dateValue;
      // JavaScript months are 0-indexed, LocalDateTime months are 1-indexed
      return new Date(year, month - 1, day, hour, minute, second);
    }
    
    // Si es string que parece un array
    if (typeof dateValue === 'string' && dateValue.includes('[')) {
      try {
        const arrayStr = dateValue.replace(/[\[\]]/g, '');
        const parts = arrayStr.split(',').map(n => parseInt(n.trim()));
        if (parts.length >= 3) {
          const [year, month, day, hour = 0, minute = 0, second = 0] = parts;
          return new Date(year, month - 1, day, hour, minute, second);
        }
      } catch (error) {
        console.warn('Error parsing date array string:', dateValue, error);
      }
    }
    
    // Fallback: intentar crear fecha directamente
    try {
      return new Date(dateValue);
    } catch (error) {
      console.warn('Could not parse date:', dateValue, error);
      return new Date();
    }
  }

  /**
   * Convierte Date a formato compatible con input datetime-local
   */
  private formatDateForInput(date: Date): string {
    if (!date || !(date instanceof Date) || isNaN(date.getTime())) {
      return new Date().toISOString().slice(0, 16);
    }
    return date.toISOString().slice(0, 16);
  }

  /**
   * Convierte Date a formato LocalDateTime para el backend
   */
  private formatDateForBackend(date: Date | string): string {
    let parsedDate: Date;
    
    if (typeof date === 'string') {
      parsedDate = new Date(date);
    } else if (date instanceof Date) {
      parsedDate = date;
    } else {
      parsedDate = new Date();
    }
    
    if (isNaN(parsedDate.getTime())) {
      parsedDate = new Date();
    }
    
    // Formato LocalDateTime: YYYY-MM-DDTHH:mm:ss
    return parsedDate.toISOString().slice(0, 19);
  }
  
  /**
   * Procesa los pagos convirtiendo las fechas del backend
   */
  private procesarPagos(pagos: any[]): Pago[] {
    return pagos.map(pago => ({
      ...pago,
      fechaPago: this.parseLocalDateTime(pago.fechaPago),
      createdAt: this.parseLocalDateTime(pago.createdAt),
      updatedAt: this.parseLocalDateTime(pago.updatedAt),
      detalles: pago.detalles ? pago.detalles.map((detalle: any) => ({
        ...detalle,
        fechaDetalle: this.parseLocalDateTime(detalle.fechaDetalle),
        createdAt: this.parseLocalDateTime(detalle.createdAt),
        updatedAt: this.parseLocalDateTime(detalle.updatedAt)
      })) : []
    }));
  }
  
  loadPagos(): void {
    this.loading = true;
    this.error = '';
  
    if (this.searchText.trim()) {
      this.subscription.add(
        this.pagoService.searchPagos(this.searchText, this.currentPage, this.pageSize).subscribe({
          next: (response: PaginatedResponse<Pago>) => {
            this.pagos = this.procesarPagos(response.content);
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.loading = false;
          },
          error: (err) => {
            this.handleError('Error al buscar los pagos', err);
          }
        })
      );
    } else {
      this.subscription.add(
        this.pagoService.getAllPagos(this.currentPage, this.pageSize, 'fechaPago').subscribe({
          next: (response: PaginatedResponse<Pago>) => {
            console.log('Respuesta del backend completa:', response);
  
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
      fechaDetalle: this.formatDateForBackend(detalle.fechaDetalle),
      notas: detalle.notas || ''
    })) : [];
    
    return {
      sociaId: pago.sociaId,
      monto: pago.monto,
      fechaPago: this.formatDateForBackend(pago.fechaPago),
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

  // Métodos helper para el template
  getFechaFormateada(fecha: Date | string): string {
    if (!fecha) return '-';
    const dateObj = fecha instanceof Date ? fecha : new Date(fecha);
    if (isNaN(dateObj.getTime())) return '-';
    return dateObj.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getFechaParaInput(fecha: Date | string): string {
    if (!fecha) return '';
    const dateObj = fecha instanceof Date ? fecha : new Date(fecha);
    return this.formatDateForInput(dateObj);
  }
}
