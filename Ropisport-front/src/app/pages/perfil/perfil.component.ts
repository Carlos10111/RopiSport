import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PerfilService } from '../../services/perfil/perfil.service';
import { Perfil } from '../../services/interfaces/perfil';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent {
  perfil: Perfil = {
    id: 1,
    nombre: '',
    email: '',
    avatarUrl: '',
  };
  nuevaPassword: string = '';

  constructor(private perfilService: PerfilService) {
    this.perfilService.getPerfil().subscribe(data => {
      this.perfil = data;
    });
  }

  cambiarPassword(nuevaPassword: string): void {
    if (!nuevaPassword) return;
    
    this.perfilService.cambiarPassword(nuevaPassword).subscribe(() => {
      alert('Contraseña cambiada con éxito');
      this.nuevaPassword = '';
    });
  }
}
