import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgForm } from '@angular/forms'; // Importa NgForm para usar [(ngModel)]
import { FormsModule } from '@angular/forms'; // Importa FormsModule para usar [(ngModel)]

@Component({
  selector: 'app-socias-form',
  standalone: true,
  imports: [CommonModule, FormsModule], // Asegúrate de importar CommonModule y FormsModule
  templateUrl: './socias-form.component.html',

})
export class SociasFormComponent {

  // Lista de socias
  socias: { 
    id: string; 
    nombre: string; 
    email: string; 
    telefono: string; 
    negocio: string; 
    descripcion: string; 
    direccion: string; 
    telefonoPersonal: string; 
    telefonoNegocio: string; 
    redesSociales: { nombre: string, url: string, enlace: string }[]; 
    web: string; 
    observaciones: string;
    cif: string;  // Añadido
    numeroCuenta: string;  // Añadido
    epigrafe: string;  // Añadido
    estado: string;  // Añadido
    fechaInicio: string;  // Añadido

    fechaBaja: string;  // Añadido
    pagos: string;  // Añadido
    logo: string;  // Añadido

  }[] = [];

  // Nueva socia
  newSocia = {
  id: '',
  nombre: '',
  email: '',
  telefono: '',
  negocio: '',
  descripcion: '',
  direccion: '',
  telefonoPersonal: '',
  telefonoNegocio: '',
  redesSociales: [] as { nombre: string, url: string, enlace: string }[],
  web: '',
  cif: '',  // Añadido
  numeroCuenta: '',  // Añadido
  epigrafe: '',  // Añadido
  estado: 'activa',  // Añadido
  fechaInicio: '',  // Añadido
  fechaBaja: '',  // Añadido
  pagos: '',  // Añadido
  observaciones: '',
  logo: ''
};
 onSubmit(form: NgForm): void {
    if (form.valid) {
      // Aquí puedes acceder a los valores del formulario
      console.log('Formulario enviado:', form.value);
      
      // Asignamos los valores del formulario a 'newSocia'
      this.newSocia = form.value;
}
 }


  // Lista de redes sociales (ejemplo)
  redesSociales: { nombre: string, url: string, enlace: string }[] = [
    { nombre: 'Facebook', url: 'https://facebook.com', enlace: 'http://enlace-fb' },
    { nombre: 'Instagram', url: 'https://instagram.com', enlace: 'http://enlace-ig' }
  ];

  // Lógica para agregar una nueva socia
  agregarSocia(): void {
    // Aquí puedes generar un id único o manejarlo según la lógica que uses
    const newId = `socia-${Date.now()}`;  // Por ejemplo, usando el timestamp como id
    this.newSocia.id = newId;
    this.socias.push({...this.newSocia}); // Agregar una nueva socia
    this.resetNewSocia(); // Resetear la socia para preparar para otra inserción si se desea
  }

  // Lógica para eliminar una socia
  eliminarSocia(index: number): void {
  // Lógica para eliminar a la socia
}

  // Lógica para editar una socia (puedes implementar un modal o un formulario con los valores actuales)
editarSocia(socia: any): void {
  console.log(socia);
}

  // Lógica para resetear los datos de la nueva socia después de agregarla
  resetNewSocia(): void {
    this.newSocia = {
      id: '',
      nombre: '',
      email: '',
      telefono: '',
      negocio: '',
      descripcion: '',
      direccion: '',
      telefonoPersonal: '',
      telefonoNegocio: '',
      redesSociales: [],
      web: '',
      observaciones: '',
      cif: '',  // Añadido
      numeroCuenta: '',  // Añadido
      epigrafe: '',  // Añadido
      estado: 'activa',  // Añadido
      fechaInicio: '',  // Añadido
      fechaBaja: '',  // Añadido
      pagos: '',  // Añadido
      logo: ''
    };
  }

  // Lógica para agregar una red social
  agregarRedSocial(): void {
    const nuevaRed = { nombre: '', url: '', enlace: '' };
    this.redesSociales.push(nuevaRed);
  }

  // Lógica para eliminar una red social
  eliminarRedSocial(i: number): void {
    if (i >= 0 && i < this.redesSociales.length) {
      this.redesSociales.splice(i, 1);
    } else {
      console.error('Índice no válido para eliminar la red social.');
    }
  }

  // Lógica para manejar el cambio del logo (subir archivo)
  onLogoChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      console.log('Nuevo archivo de logo:', input.files[0]);
    }
  }

  // Lógica para manejar el cambio de estado (activa/inactiva)
  cambiarEstado(id: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.estado = socia.estado === 'activa' ? 'inactiva' : 'activa';
      console.log('Estado cambiado:', socia);
    } else {
      console.error('Socia no encontrada para cambiar el estado');
    }
  }
  // Lógica para manejar el cambio de fecha de baja
  cambiarFechaBaja(id: string, fecha: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.fechaBaja = fecha;
      console.log('Fecha de baja cambiada:', socia);
    } else {
      console.error('Socia no encontrada para cambiar la fecha de baja');
    }
  }
  // Lógica para manejar el cambio de fecha de inicio
  cambiarFechaInicio(id: string, fecha: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.fechaInicio = fecha;
      console.log('Fecha de inicio cambiada:', socia);
    } else {
      console.error('Socia no encontrada para cambiar la fecha de inicio');
    }
  }
  // Lógica para manejar el cambio de pagos
  cambiarPagos(id: string, pagos: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.pagos = pagos;
      console.log('Pagos cambiados:', socia);
    } else {
      console.error('Socia no encontrada para cambiar los pagos');
    }
  }
  // Lógica para manejar el cambio de observaciones
  cambiarObservaciones(id: string, observaciones: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.observaciones = observaciones;
      console.log('Observaciones cambiadas:', socia);
    } else {
      console.error('Socia no encontrada para cambiar las observaciones');
    }
  }
  // Lógica para manejar el cambio de logo
  cambiarLogo(id: string, logo: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.logo = logo;
      console.log('Logo cambiado:', socia);
    } else {
      console.error('Socia no encontrada para cambiar el logo');
    }
  }
  // Lógica para manejar el cambio de CIF
  cambiarCIF(id: string, cif: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.cif = cif;
      console.log('CIF cambiado:', socia);
    } else {
      console.error('Socia no encontrada para cambiar el CIF');
    }
  }
  // Lógica para manejar el cambio de número de cuenta
  cambiarNumeroCuenta(id: string, numeroCuenta: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.numeroCuenta = numeroCuenta;
      console.log('Número de cuenta cambiado:', socia);
    } else {
      console.error('Socia no encontrada para cambiar el número de cuenta');
    }
  }
  // Lógica para manejar el cambio de epígrafe
  cambiarEpigrafe(id: string, epigrafe: string): void {
    const socia = this.socias.find(s => s.id === id);
    if (socia) {
      socia.epigrafe = epigrafe;
      console.log('Epígrafe cambiado:', socia);
    } else {
      console.error('Socia no encontrada para cambiar el epígrafe');
    }
  }

}
