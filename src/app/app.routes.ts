import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LayoutComponent} from './pages/layout/layout.component';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/login/login.component';
import {RegistroComponent} from './pages/registro/registro.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {authGuard} from './services/guards/auth.guard';
import {publicGuard} from './services/guards/public.guard';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { GruposComponent } from './pages/grupos/grupos.component';
import { EventosComponent } from './pages/eventos/eventos.component';
import { RewardsComponent } from './pages/rewards/rewards.component';
import { RolesComponent } from './pages/roles/roles.component';

export const routes: Routes = [
  // localhost:4200 -> www.ejemplo.com
  {
    path: "", component: LayoutComponent, children: [ 
      {path: "", component: HomeComponent}, 
      {path: "login", component: LoginComponent, canActivate: [publicGuard]}, 
      {path: "registro", component: RegistroComponent, canActivate: [publicGuard]},
      {path: "perfil", component: PerfilComponent },
      {path: "grupos", component: GruposComponent},
      {path: "eventos", component: EventosComponent},
      {path: "rewards", component: RewardsComponent},
      {path: "roles", component: RolesComponent},
      
    ],  
  },

  {path: "**", component: PageNotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }













