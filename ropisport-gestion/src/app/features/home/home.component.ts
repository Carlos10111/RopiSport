import { Component } from '@angular/core';
//import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
//import { FormularioBusquedaComponent } from '../formulario-busqueda/formulario-busqueda.component';

@Component({
  selector: 'app-home',
  //imports: [RouterLink, FormularioBusquedaComponent],
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})

export class HomeComponent {
  constructor(private router: Router) {}

  irASocias() {
    this.router.navigate(['/socias']);
  }

  irAInstituciones() {
    this.router.navigate(['/instituciones']);
  }
}
