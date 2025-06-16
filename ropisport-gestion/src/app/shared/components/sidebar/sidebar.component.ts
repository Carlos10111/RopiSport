import { Component, OnInit } from '@angular/core';
import { SidebarStatusService } from '../../status/sidebar-status.service';
import { UseStateService } from '../../../core/auth/use-state.service';
import { PopupService } from '../../utils/popup.service';
import { TokenInterceptor } from '../../../core/interceptors/token.interceptor';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from '../../../core/auth/token.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [RouterModule, CommonModule],
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
  isActive: boolean = false;
  isAuthenticated: boolean = false;
  currentUser: any = null;
  isActiveMenuHeader: boolean = true;

  constructor(
    private sidebarStatusService: SidebarStatusService,
    private popupService: PopupService,
    private router: Router,
    public userStateService: UseStateService,
    public tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.sidebarStatusService.status$.subscribe(status => {
      this.isActiveMenuHeader = status;
      console.log('Usuario logueado:', this.userStateService.getUsername());
      console.log('Rol:', this.userStateService.getUserRole());
    });
  }

  logout(): void {
    this.tokenService.clearAll(); // Era: this.TokenService.clearAll();
    this.isAuthenticated = false;
    this.currentUser = null;
    this.popupService.loader('Cerrando sesión...', 'Por favor, espera');

    setTimeout(() => {
      this.userStateService.removeSession();
      this.popupService.close();
      this.router.navigate(['/login']);
    }, 800)    
    
  }

  closeSidebar(): void {
    console.log('Cerrando sidebar');
  }
}