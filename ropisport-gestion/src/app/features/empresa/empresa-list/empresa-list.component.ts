import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmpresaService } from '../../../core/services/empresa/empresa.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-empresa-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empresa-list.component.html',
  styleUrls: ['./empresa-list.component.scss']
})
export class EmpresaListComponent {
  private empresaService = inject(EmpresaService);
  private router = inject(Router);
  empresas$ = this.empresaService.getAll();

  editar(id: number) {
    this.router.navigate(['/empresa/edit', id]);
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar esta empresa?')) {
      this.empresaService.delete(id).subscribe(() => {
        this.empresas$ = this.empresaService.getAll();
      });
    }
  }
}
