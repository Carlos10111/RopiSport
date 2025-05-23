import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { PublicGuard } from './core/guards/public.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HomeComponent } from './features/home/home.component';

export const routes: Routes = [
  // Ruta por defecto - redirige al login
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  
  // Rutas públicas (solo accesibles si NO está autenticado)
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
        loadComponent: () => import('./features/auth/registro/registro.component').then(m => m.RegistroComponent)
      }
    ]
  },

  // Panel de administración (requiere autenticación)
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
      {
        path: 'dashboard',
        component: HomeComponent
      },
      // Aquí puedes agregar más rutas del panel de admin
      {
        path: 'usuarios',
        loadComponent: () => import('./features/usuario/usuario-list/usuario-list.component').then(m => m.UsuarioListComponent)
      },
      {
        path: 'socias',
        loadComponent: () => import('./features/socias/socias-list/socias-list.component').then(m => m.SociasListComponent)
      }
      // ... más rutas según necesites
    ]
  },

  // Ruta para páginas no encontradas
  {
    path: '**',
    loadComponent: () => import('./page-not-found/page-not-found.component').then(m => m.PageNotFoundComponent)
  }
];