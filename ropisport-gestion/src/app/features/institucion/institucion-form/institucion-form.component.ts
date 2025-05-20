import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Institucion } from '../../../core/models/institucion';

@Component({
  selector: 'app-institucion-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './institucion-form.component.html',
})
export class InstitucionFormComponent {

  // Lista de instituciones
  instituciones: Institucion[] = [];

  // Institución nueva vacía
  newInstitucion: Institucion = this.crearInstitucionVacia();

  // Reactive form
  form: FormGroup;

  // Tipos de institución disponibles
  tiposInstitucion: { id: number; nombre: string }[] = [];

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      nombreInstitucion: ['', Validators.required],
      personaContacto: ['', Validators.required],
      cargo: [''],
      direccion: [''], // Agregado
      telefono: [''],
      email: ['', [Validators.email]],
      web: [''],
      fechaIncorporacion: [''], // Agregado
      fechaBaja: [''], // Agregado
      activa: [true], // Agregado, por defecto true
      tipoInstitucionId: [null],
      observaciones: ['']
    });
  }

  // Enviar formulario
  onSubmit(): void {
    if (this.form.valid) {
      console.log('Formulario de institución válido. Datos:', this.form.value);
    } else {
      console.warn('Formulario de institución inválido');
    }
  }
  

  // Cancelar edición
  onCancel(): void {
    console.log('Edición de institución cancelada');
    this.form.reset();
  }
  /*onCancel(): void {
    console.log('Edición cancelada');
    this.form.reset({ activa: true });
  }*/

  // Agregar institución
  agregarInstitucion(): void {
    this.newInstitucion.id = Date.now();
    this.instituciones.push({ ...this.newInstitucion });
    console.log('Institución agregada:', this.newInstitucion);
    this.resetNewInstitucion();
  }

  // Resetear nueva institución
  resetNewInstitucion(): void {
    this.newInstitucion = this.crearInstitucionVacia();
    console.log('Formulario de institución reseteado');
  }

  // Editar institución
  editarInstitucion(institucion: Institucion): void {
    console.log('Editar institución:', institucion);
  }

  // Cambiar persona de contacto
  cambiarPersonaContacto(id: number, persona: string): void {
    const institucion = this.getInstitucionById(id);
    if (institucion) {
      institucion.personaContacto = persona;
      console.log(`Persona de contacto actualizada (${id}):`, persona);
    }
  }

  // Cambiar cargo
  cambiarCargo(id: number, cargo: string): void {
    const institucion = this.getInstitucionById(id);
    if (institucion) {
      institucion.cargo = cargo;
      console.log(`Cargo actualizado (${id}):`, cargo);
    }
  }

  // Cambiar teléfono
  cambiarTelefono(id: number, telefono: string): void {
    const institucion = this.getInstitucionById(id);
    if (institucion) {
      institucion.telefono = telefono;
      console.log(`Teléfono actualizado (${id}):`, telefono);
    }
  }

  // Cambiar email
  cambiarEmail(id: number, email: string): void {
    const institucion = this.getInstitucionById(id);
    if (institucion) {
      institucion.email = email;
      console.log(`Email actualizado (${id}):`, email);
    }
  }

  // Cambiar web
  cambiarWeb(id: number, web: string): void {
    const institucion = this.getInstitucionById(id);
    if (institucion) {
      institucion.web = web;
      console.log(`Web actualizada (${id}):`, web);
    }
  }

  // Cambiar observaciones
  cambiarObservaciones(id: number, observaciones: string): void {
    const institucion = this.getInstitucionById(id);
    if (institucion) {
      institucion.observaciones = observaciones;
      console.log(`Observaciones actualizadas (${id}):`, observaciones);
    }
  }

  // Cambiar tipo de institución
  cambiarTipoInstitucion(id: number, tipoId: number): void {
    const institucion = this.getInstitucionById(id);
    const tipo = this.tiposInstitucion.find(t => t.id === tipoId);
    if (institucion && tipo) {
      institucion.tipoInstitucionId = tipo.id;
      institucion.nombreTipoInstitucion = tipo.nombre;
      console.log(`Tipo de institución actualizado (${id}):`, tipo);
    }
  }

  // Helpers
  private getInstitucionById(id: number): Institucion | undefined {
    const institucion = this.instituciones.find(i => i.id === id);
    if (!institucion) console.error(`Institución con ID ${id} no encontrada`);
    return institucion;
  }

  private crearInstitucionVacia(): Institucion {
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
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
  }
}
