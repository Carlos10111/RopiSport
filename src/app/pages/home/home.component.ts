import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormularioBusquedaComponent } from '../formulario-busqueda/formulario-busqueda.component';
import { MessageComponent } from '../message/message.component';

@Component({
  selector: 'app-home',
  imports: [RouterLink, FormularioBusquedaComponent, MessageComponent],
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
