import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-formulario-busqueda',
  templateUrl: './formulario-busqueda.component.html',
  styleUrl: './formulario-busqueda.component.scss',
  standalone: true,
  imports: [FormularioBusquedaComponent,
    ReactiveFormsModule
  ]
  
})
export class FormularioBusquedaComponent {

  productosForm: FormGroup;

  constructor(private formBuilder: FormBuilder)
  {
    this.productosForm = this.formBuilder.group({
      nombre: [''],
      identificador: [-1],
      todos: [true]
    })
  }

}
