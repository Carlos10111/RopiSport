import { Component, inject, OnInit } from '@angular/core';
import { InstitucionService } from '../../../core/services/institucion/institucion.service';
import { Institucion } from '../../../core/models/institucion';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-institucion-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './institucion-list.component.html',
  styleUrls: ['./institucion-list.component.scss']
})
export class InstitucionListComponent implements OnInit {
  instituciones$: Observable<Institucion[]>;
  errorMessage: string | null = null;  // Para mostrar mensajes de error

  constructor(private institucionService: InstitucionService) {
    this.instituciones$ = this.institucionService.getAll();
  }

  ngOnInit(): void {}

  // Eliminar una institución
  deleteInstitucion(id: number): void {
    const confirmation = window.confirm('¿Estás seguro de que deseas eliminar esta institución?');

    if (!confirmation) return;

    // Mostrar mensaje de carga mientras se realiza la eliminación
    this.errorMessage = null; // Resetear el mensaje de error

    this.institucionService.delete(id).pipe(
      catchError(error => {
        // Manejar error en la eliminación
        this.errorMessage = 'Error al eliminar la institución. Intenta nuevamente.';
        return of(null);  // Retornar un observable vacío para no romper la cadena
      })
    ).subscribe(() => {
      // Si la eliminación es exitosa, actualizamos la lista localmente sin necesidad de recargar
      this.instituciones$ = this.institucionService.getAll(); // Recargar datos
    });
  }
}


/*import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitucionService } from '../../../core/services/institucion/institucion.service';
import { Institucion } from '../../../core/models/institucion';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-institucion-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './institucion-list.component.html'
})
export class InstitucionListComponent {
  private institucionService = inject(InstitucionService);
  instituciones = signal<Institucion[]>([]);

  constructor() {
    this.institucionService.getAll().subscribe(data => this.instituciones.set(data));
  }

  eliminar(id: number) {
    this.institucionService.delete(id).subscribe(() => {
      this.instituciones.update(list => list.filter(i => i.id !== id));
    });
  }
}*/
