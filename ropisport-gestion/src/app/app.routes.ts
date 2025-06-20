import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { PublicGuard } from './core/guards/public.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { RegistroComponent } from './features/auth/registro/registro.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { UsuariosComponent } from './features/usuarios/usuarios.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { InstitucionListComponent } from './features/institucion/institucion-list/institucion-list.component';
import { PagoListComponent } from './features/pago/pago-list/pago-list.component';
import { InstitucionFormComponent } from './features/institucion/institucion-form/institucion-form.component';
import { SociasFormComponent } from './features/socias/socias-form/socias-form.component';
import { SociaListComponent } from './features/socias/socias-list/socias-list.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { EmpresaComponent } from './features/empresa/empresa-list/empresa-list.component';
import { HomeComponent } from './features/home/home.component';

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
      { path: 'dashboard', component: DashboardComponent },
      { path: 'home', component: HomeComponent},
      { path : 'empresas', component : EmpresaComponent},
      { path: 'socias', component: SociaListComponent },
      { path: 'socias-form', component: SociasFormComponent},
      { path: 'instituciones', component: InstitucionListComponent },
      { path: 'instituciones-form', component: InstitucionFormComponent },
      { path: 'pagos', component: PagoListComponent},
      { path: 'usuarios', component: UsuariosComponent },
    ]
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];