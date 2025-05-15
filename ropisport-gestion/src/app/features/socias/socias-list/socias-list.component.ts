import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importa FormsModule para usar [(ngModel)]
import { NgForm } from '@angular/forms'; // Importa NgForm para usar [(ngModel)]

@Component({
  selector: 'app-socias-list',
  templateUrl: './socias-list.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule] // Asegúrate de importar CommonModule y FormsModule
})
export class SociasListComponent {
  socias = [
    {
      id: 1,
      nombre: 'Ana Gómez',
      email: 'ana@example.com',
      telefono: '600123123',
      negocio: 'Tienda Ana',
      descripcion: 'Moda y complementos',
      direccion: 'Calle Mayor 1',
      telefonoPersonal: '600111111',
      telefonoNegocio: '600222222',
      redesSociales: [{ nombre: 'Instagram' }, { nombre: 'Facebook' }],
      web: 'https://tiendaana.com',
      cif: 'B12345678',
      numeroCuenta: 'ES7620770024003102575766',
      epigrafe: 'Comercio',
      estado: 'baja',
      fechaInicio: '2023-01-15',
      fechaBaja: '',
      pagos: 'Mensual',
      observaciones: '',
      logo: ''
    }
  ];
  editarSocia(socia: any) {
  console.log('Editar:', socia);
  // Aquí podrías navegar al formulario o abrir un modal
}

eliminarSocia(index: number) {
  if (confirm('¿Estás seguro de eliminar esta socia?')) {
    this.socias.splice(index, 1);
  }
}

}
