import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError, finalize } from 'rxjs/operators';
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
    // Limpiar suscripciones para prevenir memory leaks
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
    expandido: false, // 👈 Importante para la funcionalidad de expansión
    fechaInicio: new Date(), // debe coincidir con el backend
    fechaBaja: null,
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
        this.socias = response.content.map(socia => ({
          ...socia,
          descripcionNegocio: socia.descripcionNegocio || '',
          direccion: socia.direccion || '',
          telefonoNegocio: socia.telefonoNegocio || '',
          cif: socia.cif || '',
          numeroCuenta: socia.numeroCuenta || '',
          epigrafe: socia.epigrafe || '',
          observaciones: socia.observaciones || '',
          expandido: false // Inicializar expandido en false
        }));
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;

        // Calcular el último número asignado
        const max = this.socias.reduce((acc, s) => {
          const numero = Number(s.numeroSocia); // asegura que sea un número
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
  
  // FUNCIONES PARA MODALES
  
  abrirModalNueva(): void {
    this.sociaActual = this.inicializarSocia();

      // Calcular el número más alto actual
  const maxNumero = this.socias.reduce((max, s) => {
    const num = Number(s.numeroSocia);
    return !isNaN(num) && num > max ? num : max;
  }, 0);

  // Asignar el siguiente número como string
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

  // Asegurarse de que fechaAlta sea un objeto Date para el input[type=date]
  if (this.sociaActual.fechaInicio) {
    this.sociaActual.fechaInicio = new Date(this.sociaActual.fechaInicio);
  }

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
  private formatFechaInicioParaBackend(date: Date | string | null): string | null {
  if (!date) return null;

  const dateObj = date instanceof Date ? date : new Date(date);
  if (isNaN(dateObj.getTime())) return null;
  return dateObj.toISOString();

  const pad = (n: number) => (n < 10 ? '0' + n : n);

  const day = pad(dateObj.getDate());
  const month = pad(dateObj.getMonth() + 1);
  const year = dateObj.getFullYear();

  const hours = pad(dateObj.getHours());
  const minutes = pad(dateObj.getMinutes());

  return `${day}/${month}/${year} ${hours}:${minutes}`;
}

guardarNuevaSocia(): void {
  this.guardando = true;
  this.error = '';
  

  const fechaInicioFormateada = this.formatFechaInicioParaBackend(this.sociaActual.fechaInicio || new Date());
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
    fechaInicio: fechaInicioFormateada,    
    //fechaBaja: null,
  };
  console.log('Payload al crear socia:', payload);

  this.sociaService.createSocia(payload).subscribe({
    next: (response) => {
      this.socias.push(response);
      this.cerrarModal();
      this.guardando = false;
    },
    error: (err) => {
      this.guardando = false;
      this.error = 'Error al crear la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
    }
  });
}




private formatDateTime(date: Date | string | null): string | null {
  if (!date) return null;
  const dateObj = date instanceof Date ? date : new Date(date);
  if (isNaN(dateObj.getTime())) return null;
  return dateObj.toISOString(); // formato ISO 8601 completo
}

get formattedFechaInicio(): string | null {
  if (!this.sociaActual.fechaInicio) return null;
  // Si es Date, conviértelo; si ya es string en formato correcto, devuélvelo tal cual
  if (typeof this.sociaActual.fechaInicio === 'string') return this.sociaActual.fechaInicio;
  return new Date(this.sociaActual.fechaInicio).toISOString().substring(0, 10);
}

set formattedFechaInicio(value: string | null) {
  this.sociaActual.fechaInicio = value;
}


 get formattedFechaBaja(): string | null {
  if (!this.sociaActual.fechaBaja) return null;
  // Si es Date, conviértelo; si ya es string en formato correcto, devuélvelo tal cual
  if (typeof this.sociaActual.fechaBaja === 'string') return this.sociaActual.fechaBaja;
  return new Date(this.sociaActual.fechaBaja).toISOString().substring(0, 10);
}

set formattedFechaBaja(value: string | null) {
  this.sociaActual.fechaBaja = value;
}

private formatDateTimeForBackend(date: Date | string | null): string | null {
  if (!date) return null;

  const dateObj = date instanceof Date ? date : new Date(date);
  if (isNaN(dateObj.getTime())) return null;
  return dateObj.toISOString();

  const pad = (n: number) => (n < 10 ? '0' + n : n);

  const day = pad(dateObj.getDate());
  const month = pad(dateObj.getMonth() + 1);
  const year = dateObj.getFullYear();

  const hours = pad(dateObj.getHours());
  const minutes = pad(dateObj.getMinutes());

  return `${day}/${month}/${year} ${hours}:${minutes}`;
}


guardarEdicionSocia(): void {
  if (this.guardando) return;

  if (!this.sociaActual.id) {
    this.error = 'No se puede actualizar: ID de socia no definido.';
    return;
  }

  this.guardando = true;
  this.error = '';

  const fechaInicioFormateada = this.formatDateTimeForBackend(this.sociaActual.fechaInicio || new Date());
  const fechaBajaFormateada = this.formatDateTimeForBackend(this.sociaActual.fechaBaja || null);
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
    fechaInicio: this.formatDateTimeForBackend(this.sociaActual.fechaInicio ?? null),
    fechaBaja: this.formatDateTimeForBackend(this.sociaActual.fechaBaja ?? null),
    categoriaId: this.sociaActual.categoria ? { id: this.sociaActual.categoria.id } : null,
  };

  // Eliminar propiedades nulas para evitar enviarlas
  Object.keys(payload).forEach(key => {
    if (payload[key] === null || payload[key] === undefined) {
      delete payload[key];
    }
  });

  this.sociaService.updateSocia(this.sociaActual.id, payload).subscribe({
    next: (response) => {
      this.loadSocias();
      this.cerrarModal();
      this.guardando = false;
      // Aquí podrías mostrar un toast o notificación de éxito
    },
    error: (err) => {
      console.error('Error actualizando socia:', err);
      this.guardando = false;
      this.error = 'Error al actualizar la socia: ' + (err.error?.message || 'Ocurrió un problema en el servidor');
    }
  });
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