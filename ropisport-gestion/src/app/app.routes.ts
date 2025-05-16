import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegistroComponent } from './features/auth/registro/registro.component';
import { ChangePasswordComponent } from './features/auth/change-password/change-password.component';
import { authGuard } from './core/guards/auth.guard';
import { publicGuard } from './core/guards/public.guard';
import { InstitucionListComponent } from './features/institucion/institucion-list/institucion-list.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { InstitucionFormComponent } from './features/institucion/institucion-form/institucion-form.component';
import { SociasFormComponent } from './features/socias/socias-form/socias-form.component';
import { SociasListComponent } from './features/socias/socias-list/socias-list.component';
export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    //canActivate: [authGuard],       // rutas privadas (protegidas por AuthGuard)
    children: [
      { path: '', component: HomeComponent },
      //{ path: 'instituciones', component: InstitucionListComponent },
    //]
  //},
  //{
    //path: '',
    //component: AuthLayoutComponent,
    //canActivate: [publicGuard],
    //children: [
      { path: 'institucion-list', component: InstitucionListComponent },
      { path: 'institucion-form', component: InstitucionFormComponent },
      { path: 'login', component: LoginComponent },
      { path: 'registro', component: RegistroComponent },
      { path: 'cambiar-password', component: ChangePasswordComponent },
      { path: 'formulario', component: SociasFormComponent },
      { path: 'listado', component: SociasListComponent },  
      // Más rutas públicas si lo necesitas
    ]
  },
  {
    path: "**", component: PageNotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }


