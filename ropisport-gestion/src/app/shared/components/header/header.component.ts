import { Component, OnInit } from '@angular/core';
import { SidebarStatusService } from '../../status/sidebar-status.service';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { TokenInterceptor } from '../../../core/interceptors/token.interceptor';
import { UseStateService } from '../../../core/auth/use-state.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [RouterLink, CommonModule],
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  isActive: boolean = false;
  isAuthenticated: boolean = false;
  currentUser: any = null;

  // Variables de tabs
  isActiveItems: any = {
    isActiveNotification: false,
    isActiveSettings: false,
    isActiveProfile: false
  }

  constructor(
    private sidebarStatusService: SidebarStatusService,
    private router: Router,
    private tokenInterceptor: TokenInterceptor,
    private useStateService: UseStateService
  ) {}

  ngOnInit(): void {
    // Verificar el estado de autenticación al cargar el componente
    this.checkAuthStatus();
  }

  checkAuthStatus(): void {
    this.isAuthenticated = this.tokenInterceptor.isAuthenticated();
    if (this.isAuthenticated) {
      // Obtener datos del usuario si está autenticado
      this.currentUser = this.useStateService.getCurrentUser(); // Asumiendo que tienes este método
    }
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  goToRegister(): void {
    this.router.navigate(['/registro']);
  }

  goToAdmin(): void {
    this.router.navigate(['/admin']);
  }

  goToProfile(): void {
    this.router.navigate(['/admin/perfil']); // o la ruta que tengas para el perfil
  }

  logout(): void {
    this.tokenInterceptor.clearTokens();
    this.useStateService.clear(); // Si tienes este método
    this.isAuthenticated = false;
    this.currentUser = null;
    this.router.navigate(['/login']);
  }

  toggleItem(option: string): void {
    if (this.isActiveItems[option]) {
      this.isActiveItems[option] = false;
    } else {
      Object.keys(this.isActiveItems).forEach((item) => {
        this.isActiveItems[item] = false;
      });
      this.isActiveItems[option] = true;
    }
  }

  toggleSidebar(): void {
    this.isActive = !this.isActive;
    this.sidebarStatusService.changeStatus(this.isActive);
  }
}