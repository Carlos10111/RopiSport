import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SociasFormComponent } from './features/socias/socias-form/socias-form.component'; // Asegúrate de la ruta correcta
import { SociasListComponent } from './features/socias/socias-list/socias-list.component'; // Asegúrate de la ruta correcta
import { FormsModule } from '@angular/forms';

const routes: Routes = [
  { path: '', component: SociasFormComponent },
  { path: '', component: SociasListComponent }, // Ruta para el formulario de socias
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes),
    RouterModule.forChild(routes) // Asegúrate de que esto esté aquí,
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
