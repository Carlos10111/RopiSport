import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
import { SociaService } from '../../../core/services/socia/socia.service';
import { Socia } from '../../../core/models/socia';
import { PaginatedResponse } from '../../../core/models/paginated-response';
import * as XLSX from 'xlsx';

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
  ultimoNumeroSociaAsignado = 0;

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
    this.socias = this.socias.map(socia => ({
      ...socia,
      expandido: false
    }));

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
    this.subscription.unsubscribe();
  }
  
  inicializarSocia(): Socia {
    return {
      nombre: '',
      apellidos: '',
      telefonoPersonal: '', 
      email: '',  
      nombreNegocio: '',
      activa: true,
      descripcionNegocio: '',
      direccion: '',
      telefonoNegocio: '',
      cif: '',
      numeroCuenta: '',
      epigrafe: '',
      observaciones: '',
      expandido: false,
      fechaInicio: new Date(),
      fechaBaja: null,
    };
  }

  // ✅ MÉTODO CORREGIDO - Sin logs duplicados
  loadSocias(): void {
    this.loading = true;
    this.error = '';

    const request = this.searchText
      ? this.sociaService.searchSocias(this.searchText, this.currentPage, this.pageSize)
      : this.sociaService.getAllSocias(this.currentPage, this.pageSize);

    this.subscription.add(
      request.subscribe({
        next: (response: PaginatedResponse<Socia>) => {
          // ✅ Log para debug - Ver respuesta del backend
          console.log('📊 Respuesta completa del backend:', response);
          console.log('📊 Primera socia cruda:', response.content[0]);
          
          this.socias = response.content.map(socia => ({
            ...socia,
            descripcionNegocio: socia.descripcionNegocio || '',
            direccion: socia.direccion || '',
            telefonoNegocio: socia.telefonoNegocio || '',
            cif: socia.cif || '',
            numeroCuenta: socia.numeroCuenta || '',
            epigrafe: socia.epigrafe || '',
            observaciones: socia.observaciones || '',
            fechaBaja: socia.fechaBaja || null,
            expandido: false
          }));
          
          // ✅ Log después del mapeo
          console.log('📊 Primera socia después del mapeo:', this.socias[0]);
          console.log('📅 fechaBaja específicamente:', this.socias[0]?.fechaBaja);
          console.log('📊 Todas las fechas de baja:', this.socias.map(s => ({ id: s.id, fechaBaja: s.fechaBaja })));
          
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;

          const max = this.socias.reduce((acc, s) => {
            const numero = Number(s.numeroSocia);
            return !isNaN(numero) && numero > acc ? numero : acc;
          }, 0);
          this.ultimoNumeroSociaAsignado = max;
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
  
  // AÑADIR - Métodos para dar de baja y reactivar
  darDeBaja(socia: Socia): void {
    if (!socia.id) return;
    
    const observaciones = 'Dada de baja desde la gestión';
    this.sociaService.cambiarEstado(socia.id, false, observaciones).subscribe({
      next: () => {
        console.log('Socia dada de baja correctamente');
        this.loadSocias(); // Recargar la lista
      },
      error: (err) => {
        console.error(' Error al dar de baja:', err);
        this.error = 'Error al dar de baja la socia';
      }
    });
  }

  reactivar(socia: Socia): void {
    if (!socia.id) return;
    
    const observaciones = 'Reactivada desde la gestión';
    this.sociaService.cambiarEstado(socia.id, true, observaciones).subscribe({
      next: () => {
        console.log('Socia reactivada correctamente');
        this.loadSocias(); // Recargar la lista
      },
      error: (err) => {
        console.error('Error al reactivar:', err);
        this.error = 'Error al reactivar la socia';
      }
    });
  }
  
  // FUNCIONES PARA MODALES
  abrirModalNueva(): void {
    this.sociaActual = this.inicializarSocia();

    const maxNumero = this.socias.reduce((max, s) => {
      const num = Number(s.numeroSocia);
      return !isNaN(num) && num > max ? num : max;
    }, 0);

    this.sociaActual.numeroSocia = String(maxNumero + 1);
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
  
  seleccionarSociaParaEditar(socia: Socia) {
    this.sociaActual = { ...socia };

    if (this.sociaActual.fechaInicio) {
      this.sociaActual.fechaInicio = new Date(this.sociaActual.fechaInicio);
    }
    if (this.sociaActual.fechaBaja) {
      this.sociaActual.fechaBaja = new Date(this.sociaActual.fechaBaja);
    }

    this.modalActivo = 'editar';
  }
  
  seleccionarSociaParaEliminar(socia: Socia): void {
    this.sociaActual = socia;
    this.modalActivo = 'eliminar';
  }
  
abrirModalEditar(socia: Socia): void {
  this.sociaActual = { ...socia };
  
  // ✅ MEJORAR: Convertir fechas correctamente
  if (this.sociaActual.fechaInicio) {
    this.sociaActual.fechaInicio = new Date(this.sociaActual.fechaInicio);
  }
  if (this.sociaActual.fechaBaja) {
    this.sociaActual.fechaBaja = new Date(this.sociaActual.fechaBaja);
  }
  
  console.log('📅 Socia para editar:', {
    id: this.sociaActual.id,
    nombre: this.sociaActual.nombre,
    fechaInicio: this.sociaActual.fechaInicio,
    fechaBaja: this.sociaActual.fechaBaja,
    activa: this.sociaActual.activa
  });
  
  this.modalActivo = 'editar';
}
  abrirModalEliminar(socia: Socia): void {
    this.sociaActual = socia;
    this.modalActivo = 'eliminar';
  }
  
  cerrarModal(): void {
    this.modalActivo = null;
    this.guardando = false;
  }

  guardarNuevaSocia(): void {
    this.guardando = true;
    this.error = '';

    const payload = {
      nombre: this.sociaActual.nombre,
      apellidos: this.sociaActual.apellidos,
      telefonoPersonal: this.sociaActual.telefonoPersonal,
      email: this.sociaActual.email,
      nombreNegocio: this.sociaActual.nombreNegocio,
      descripcionNegocio: this.sociaActual.descripcionNegocio,
      direccion: this.sociaActual.direccion,
      telefonoNegocio: this.sociaActual.telefonoNegocio,
      cif: this.sociaActual.cif,
      numeroCuenta: this.sociaActual.numeroCuenta,
      epigrafe: this.sociaActual.epigrafe,
      observaciones: this.sociaActual.observaciones,
      activa: this.sociaActual.activa ?? true,
      numeroSocia: this.sociaActual.numeroSocia,
      fechaInicio: this.formatDateForBackend(this.sociaActual.fechaInicio || new Date()),
      categoriaId: this.sociaActual.categoria?.id || null
    };

    console.log('📤 Payload al crear socia:', payload);

    this.sociaService.createSocia(payload).subscribe({
      next: (response) => {
        console.log('✅ Socia creada correctamente');
        this.loadSocias();
        this.cerrarModal();
        this.guardando = false;
      },
      error: (err) => {
        console.error('❌ Error al crear socia:', err);
        this.guardando = false;
        this.error = 'Error al crear la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
      }
    });
  }

  get formattedFechaInicio(): string | null {
    if (!this.sociaActual.fechaInicio) return null;
    if (typeof this.sociaActual.fechaInicio === 'string') return this.sociaActual.fechaInicio;
    return new Date(this.sociaActual.fechaInicio).toISOString().substring(0, 10);
  }

  set formattedFechaInicio(value: string | null) {
    this.sociaActual.fechaInicio = value;
  }

  get formattedFechaBaja(): string | null {
    if (!this.sociaActual.fechaBaja) return null;
    if (typeof this.sociaActual.fechaBaja === 'string') return this.sociaActual.fechaBaja;
    return new Date(this.sociaActual.fechaBaja).toISOString().substring(0, 10);
  }

  set formattedFechaBaja(value: string | null) {
    this.sociaActual.fechaBaja = value;
  }

// src/app/features/socias/socias-list/socias-list.component.ts

guardarEdicionSocia(): void {
  if (this.guardando) return;

  if (!this.sociaActual.id) {
    this.error = 'No se puede actualizar: ID de socia no definido.';
    return;
  }

  this.guardando = true;
  this.error = '';

  const payload: any = {
    nombre: this.sociaActual.nombre,
    apellidos: this.sociaActual.apellidos,
    telefonoPersonal: this.sociaActual.telefonoPersonal,
    email: this.sociaActual.email,
    nombreNegocio: this.sociaActual.nombreNegocio,
    descripcionNegocio: this.sociaActual.descripcionNegocio,
    direccion: this.sociaActual.direccion,
    telefonoNegocio: this.sociaActual.telefonoNegocio,
    cif: this.sociaActual.cif,
    numeroCuenta: this.sociaActual.numeroCuenta,
    epigrafe: this.sociaActual.epigrafe,
    observaciones: this.sociaActual.observaciones,
    activa: this.sociaActual.activa,
    numeroSocia: this.sociaActual.numeroSocia,
    fechaInicio: this.formatDateForBackend(this.sociaActual.fechaInicio),
    // AGREGAR: Incluir fechaBaja en el payload
    fechaBaja: this.formatDateForBackend(this.sociaActual.fechaBaja),
    categoriaId: this.sociaActual.categoria?.id || null
  };

  // MODIFICAR: No eliminar fechaBaja si es null (puede ser intencional)
  Object.keys(payload).forEach(key => {
    if (payload[key] === undefined) { // Solo eliminar undefined, no null
      delete payload[key];
    }
  });

  console.log('📤 Payload para actualización:', payload);

  this.sociaService.updateSocia(this.sociaActual.id, payload).subscribe({
    next: (response) => {
      console.log('Socia actualizada correctamente');
      this.loadSocias();
      this.cerrarModal();
      this.guardando = false;
    },
    error: (err) => {
      console.error('Error actualizando socia:', err);
      this.guardando = false;
      this.error = 'Error al actualizar la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
    }
  });
}

  private formatDateForBackend(date: Date | string | null | undefined): string | null {
    if (!date) return null;
    
    try {
      const dateObj = date instanceof Date ? date : new Date(date);
      if (isNaN(dateObj.getTime())) return null;
      
      let isoString = dateObj.toISOString();
      if (isoString.endsWith('Z')) {
        isoString = isoString.substring(0, isoString.length - 1);
      }
      
      return isoString;
    } catch (error) {
      console.warn('Error al formatear fecha:', date, error);
      return null;
    }
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

  // Exportar tabla a Excel
  exportarExcel(): void {
    if (this.socias.length === 0) {
      alert('No hay datos para exportar');
      return;
    }

    // Preparar los datos para Excel
    const datosParaExcel = this.socias.map(socia => ({
      'Número Socia': socia.numeroSocia || '',
      'Nombre': socia.nombre || '',
      'Apellidos': socia.apellidos || '',
      'Nombre Negocio': socia.nombreNegocio || '',
      'Descripción Negocio': socia.descripcionNegocio || '',
      'Dirección': socia.direccion || '',
      'Teléfono Personal': socia.telefonoPersonal || '',
      'Teléfono Negocio': socia.telefonoNegocio || '',
      'Email': socia.email || '',
      'CIF': socia.cif || '',
      'Número Cuenta': socia.numeroCuenta || '',
      'Epígrafe': socia.epigrafe || '',
      'Activa': socia.activa ? 'Sí' : 'No',
      'Fecha Inicio': this.getFechaFormateada(socia.fechaInicio),
      'Fecha Baja': this.getFechaFormateada(socia.fechaBaja),
      'Categoría': socia.categoria?.nombre || '',
      'Observaciones': socia.observaciones || '',
    }));

    // Crear workbook y worksheet
    const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(datosParaExcel);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    
    // Agregar worksheet al workbook
    XLSX.utils.book_append_sheet(wb, ws, 'Socias');
    
    // Generar nombre del archivo con fecha actual
    const fechaActual = new Date().toLocaleDateString('es-ES').replace(/\//g, '-');
    const nombreArchivo = `socias_${fechaActual}.xlsx`;
    
    // Descargar archivo
    XLSX.writeFile(wb, nombreArchivo);
  }

  private getFechaFormateada(fecha: Date | string | null | undefined): string {
    if (!fecha) return '';
    
    try {
      const fechaObj = typeof fecha === 'string' ? new Date(fecha) : fecha;
      return fechaObj.toLocaleDateString('es-ES');
    } catch (error) {
      return '';
    }
  }



}