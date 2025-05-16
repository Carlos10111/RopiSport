import { Component, OnInit } from '@angular/core';
import { CategoriaService } from '../../../core/services/categoriaNegocio/categoria.service';
import { Categoria } from '../../../core/models/categoriaNegocio';

@Component({
  selector: 'app-categoria-list',
  templateUrl: './categoria-list.component.html',
  standalone: true,
  styleUrls: ['./categoria-list.component.scss']
})
export class CategoriaListComponent implements OnInit {
  categorias: Categoria[] = [];

  constructor(private categoriaService: CategoriaService) {}

  ngOnInit(): void {
    this.categoriaService.getAll().subscribe(data => this.categorias = data);
  }
}