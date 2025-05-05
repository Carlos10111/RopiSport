import { Component, OnInit } from '@angular/core';
import { GrupoService } from '../../services/grupo/grupo.service';
import { Grupo } from '../../services/interfaces/grupo';

@Component({
  selector: 'app-grupos',
  imports: [],
  templateUrl: './grupos.component.html',
  styleUrl: './grupos.component.scss'
})

export class GruposComponent implements OnInit {
  grupos: Grupo[] = [];

  constructor(private grupoService: GrupoService) {}

  ngOnInit(): void {
    this.grupoService.getGrupos().subscribe(data => {
      this.grupos = data;
    });
  }
}




/*  constructor() { }

  ngOnInit(): void {
    this.getProducts();
  };

  getProducts() {
    this.productosCarritoService.getAllProducts().subscribe((data) => {
      return this.products = data;

    })
  }

*/