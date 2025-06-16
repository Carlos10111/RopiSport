import { Component, OnInit } from '@angular/core';
import { SidebarStatusService } from '../../status/sidebar-status.service';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';
import { UseStateService } from '../../../core/auth/use-state.service';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../../core/auth/token.service';
import { PopupService } from '../../utils/popup.service';

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
    private tokenService: TokenService,
    private popupService: PopupService,
    private useStateService: UseStateService
  ) {}

  ngOnInit(): void {
    // Verificar el estado de autenticación al cargar el componente
    this.checkAuthStatus();
  }

  checkAuthStatus(): void {

    this.isAuthenticated = this.tokenService.isAuthenticated(); 
    
    if (this.isAuthenticated) {
      // Obtener datos del usuario si está autenticado
      this.currentUser = this.useStateService.getCurrentUser();
    }
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
  //goToRegister()
  
  goToAdmin(): void {
    this.router.navigate(['/admin']);
  }

  goToProfile(): void {
    this.router.navigate(['/admin/perfil']);
  }

  logout(): void {
    this.tokenService.clearAll(); // Era: this.TokenService.clearAll();
    this.isAuthenticated = false;
    this.currentUser = null;
    this.popupService.loader('Cerrando sesión...', 'Por favor, espera');

    setTimeout(() => {
      this.useStateService.removeSession();
      this.popupService.close();
      this.router.navigate(['/login']);
    }, 800) 
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