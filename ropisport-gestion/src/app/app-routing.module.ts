import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SociasFormComponent } from './features/socias/socias-form/socias-form.component'; // Asegúrate de la ruta correcta
import { SociasListComponent } from './features/socias/socias-list/socias-list.component'; // Asegúrate de la ruta correcta

const routes: Routes = [
  { path: '', component: SociasFormComponent },
  { path: '', component: SociasListComponent }, // Ruta para el formulario de socias
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
