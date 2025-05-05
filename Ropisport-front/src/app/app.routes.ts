import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LayoutComponent} from './pages/layout/layout.component';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/login/login.component';
import {RegistroComponent} from './pages/registro/registro.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {authGuard} from './services/guards/auth.guard';
import {publicGuard} from './services/guards/public.guard';
import { RolesComponent } from './pages/roles/roles.component';
import { SociasComponent } from './pages/socias/socias.component';
import { ProveedoresComponent } from './pages/proveedores/proveedores.component';

export const routes: Routes = [
  // localhost:4200 -> www.ejemplo.com
  {
    path: "", component: LayoutComponent, children: [ 
      {path: "", component: HomeComponent}, 
      {path: "login", component: LoginComponent, canActivate: [publicGuard]}, 
      {path: "registro", component: RegistroComponent, canActivate: [publicGuard]},
      {path: "roles", component: RolesComponent},
      {path: "socias", component: SociasComponent},
      {path: "proveedores", component: ProveedoresComponent}
    ],  
  },

  {path: "**", component: PageNotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }













