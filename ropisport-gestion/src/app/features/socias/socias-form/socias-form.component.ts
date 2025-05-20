import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgForm, FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Socia } from '../../../core/models/socia';

@Component({
  selector: 'app-socias-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './socias-form.component.html',
})
export class SociasFormComponent {

  // Lista de socias existentes
  socias: Socia[] = [];

  // Modelo de nueva socia
  newSocia: Socia = this.crearSociaVacia();

  // Redes sociales
  redesSociales: RedSocial[] = [
    { nombre: 'Facebook', url: 'https://facebook.com', enlace: 'http://enlace-fb' },
    { nombre: 'Instagram', url: 'https://instagram.com', enlace: 'http://enlace-ig' }
  ];

  // Reactive form y categorías
  form: FormGroup;
  categorias: { id: number; nombre: string }[] = [];

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      numeroSocia: [''],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      nombreNegocio: ['', Validators.required],
      descripcionNegocio: [''],
      direccion: [''],
      telefonoPersonal: [''],
      telefonoNegocio: [''],
      email: [''],
      cif: [''],
      numeroCuenta: [''],
      epigrafe: [''],
      fechaInicio: [''],
      fechaBaja: [''],
      activa: [true],
      observaciones: [''],
      categoriaId: [null]
    });
  }

  // Enviar formulario
  onSubmit(): void {
    if (this.form.valid) {
      console.log('Formulario válido. Datos:', this.form.value);
    } else {
      console.warn('Formulario inválido');
    }
  }

  onCancel(): void {
    console.log('Formulario cancelado');
    this.form.reset();
  }

  // Agregar nueva socia
  agregarSocia(): void {
    this.newSocia.id = Date.now();
    this.socias.push({ ...this.newSocia });
    console.log('Socia agregada:', this.newSocia);
    this.resetNewSocia();
  }

  // Resetear socia temporal
  resetNewSocia(): void {
    this.newSocia = this.crearSociaVacia();
    console.log('Formulario de socia reseteado');
  }

  // Agregar red social
  agregarRedSocial(): void {
    this.redesSociales.push({ nombre: '', url: '', enlace: '' });
    console.log('Red social añadida');
  }

  // Eliminar red social
  eliminarRedSocial(i: number): void {
    if (i >= 0 && i < this.redesSociales.length) {
      this.redesSociales.splice(i, 1);
      console.log(`Red social eliminada en índice ${i}`);
    } else {
      console.error('Índice inválido al eliminar red social');
    }
  }

  // Cambiar logo
  onLogoChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files?.[0]) {
      console.log('Logo cargado:', input.files[0]);
    }
  }

  // Métodos de edición/cambio de datos
  editarSocia(socia: Socia): void {
    console.log('Editar socia:', socia);
  }

  cambiarEstado(id: number): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.activa = !socia.activa;
      console.log(`Estado cambiado (${id}):`, socia.activa);
    }
  }

  cambiarFechaBaja(id: number, fecha: string): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.fechaBaja = fecha;
      console.log(`Fecha de baja cambiada (${id}):`, fecha);
    }
  }

  cambiarFechaInicio(id: number, fecha: string): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.fechaInicio = fecha;
      console.log(`Fecha de inicio cambiada (${id}):`, fecha);
    }
  }

  cambiarObservaciones(id: number, observaciones: string): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.observaciones = observaciones;
      console.log(`Observaciones actualizadas (${id}):`, observaciones);
    }
  }

  cambiarCIF(id: number, cif: string): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.cif = cif;
      console.log(`CIF actualizado (${id}):`, cif);
    }
  }

  cambiarNumeroCuenta(id: number, numeroCuenta: string): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.numeroCuenta = numeroCuenta;
      console.log(`Número de cuenta actualizado (${id}):`, numeroCuenta);
    }
  }

  cambiarEpigrafe(id: number, epigrafe: string): void {
    const socia = this.getSociaById(id);
    if (socia) {
      socia.epigrafe = epigrafe;
      console.log(`Epígrafe actualizado (${id}):`, epigrafe);
    }
  }

  cambiarLogo(id: number, logo: string): void {
    console.warn('Método cambiarLogo no implementado directamente.');
  }

  cambiarPagos(id: number, pagos: string): void {
    console.warn('Método cambiarPagos ya no aplica al modelo actual');
  }

  // Helpers
  private getSociaById(id: number): Socia | undefined {
    const socia = this.socias.find(s => s.id === id);
    if (!socia) console.error(`Socia con ID ${id} no encontrada`);
    return socia;
  }

  private crearSociaVacia(): Socia {
    return {
      id: 0,
      numeroSocia: '',
      nombre: '',
      apellidos: '',
      nombreNegocio: '',
      descripcionNegocio: '',
      direccion: '',
      telefonoPersonal: '',
      telefonoNegocio: '',
      email: '',
      cif: '',
      numeroCuenta: '',
      epigrafe: '',
      activa: true,
      fechaInicio: '',
      fechaBaja: null,
      observaciones: '',
      categoria: { id: 0, nombre: '' }
    };
  }
}

interface RedSocial {
  nombre: string;
  url: string;
  enlace: string;
}