import { Routes } from '@angular/router';
import { SociasFormComponent } from './features/socias/socias-form/socias-form.component'; // Asegúrate de la ruta correcta
import { SociasListComponent } from './features/socias/socias-list/socias-list.component'; // Asegúrate de la ruta correcta

export const routes: Routes = [
  { path: 'formulario', component: SociasFormComponent },
  { path: 'listado', component: SociasListComponent }, // Ruta para el formulario de socias
  { path: '**', redirectTo: '' }
];
