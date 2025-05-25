import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { PublicGuard } from './core/guards/public.guard';

import { LoginComponent } from './features/auth/login/login.component';
import { RegistroComponent } from './features/auth/registro/registro.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HomeComponent } from './features/home/home.component';
import { UsuarioListComponent } from './features/usuario/usuario-list/usuario-list.component';
import { SociasListComponent } from './features/socias/socias-list/socias-list.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { InstitucionListComponent } from './features/institucion/institucion-list/institucion-list.component';
import { PagoListComponent } from './features/pago/pago-list/pago-list.component';
import { InstitucionFormComponent } from './features/institucion/institucion-form/institucion-form.component';
import { SociasFormComponent } from './features/socias/socias-form/socias-form.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },

  {
    path: '',
    component: AuthLayoutComponent,
    canActivate: [PublicGuard],
    children: [
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'registro',
        component: RegistroComponent
      }
    ]
  },

  {
    path: 'admin',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      { path: 'dashboard', component: HomeComponent },
      { path: 'usuarios', component: UsuarioListComponent },

      { path: 'socias', component: SociasListComponent },
      { path: 'socias-form', component: SociasFormComponent},

      { path: 'instituciones', component: InstitucionListComponent },
      { path: 'instituciones-form', component: InstitucionFormComponent },
      { path: 'pagos', component: PagoListComponent}
    ]
  },

  {
    path: '**',
    component: PageNotFoundComponent
  }
];
