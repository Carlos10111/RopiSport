import { Component, OnInit } from '@angular/core';
import { Socia } from '../../../core/models/socia';
import { SociaService } from '../../../core/services/socia/socia.service';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
//       const reader = new FileReader();{}

@Component({
  selector: 'app-socias-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './socias-list.component.html',
  styleUrls: ['./socias-list.component.scss']
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