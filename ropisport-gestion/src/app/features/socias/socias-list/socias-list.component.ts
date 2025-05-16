import { Component, OnInit } from '@angular/core';
import { Socia } from '../../../core/models/socia';
import { SociaService } from '../../../core/services/socia/socia.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-socias-list',
  templateUrl: './socias-list.component.html',
  styleUrls: ['./socias-list.component.css']
})
export class SociasListComponent implements OnInit {
  socias: Socia[] = [];

  constructor(private sociaService: SociaService) {}

  ngOnInit(): void {
    this.sociaService.socias$.subscribe(data => {
      this.socias = data;
    });
    this.sociaService.getSocias();
  }

  editarSocia(socia: Socia) {
    console.log('Editar:', socia);
  }

  eliminarSocia(index: number) {
    if (confirm('¿Estás seguro de eliminar esta socia?')) {
      this.socias.splice(index, 1);
    }
  }
}