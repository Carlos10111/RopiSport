import { Component, OnInit } from '@angular/core';
import { SidebarStatusService } from '../../status/sidebar-status.service';
import { UseStateService } from '../../../core/auth/use-state.service';
import { PopupService } from '../../utils/popup.service';
import { TokenInterceptor } from '../../../core/interceptors/token.interceptor';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterModule],
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
    private tokenInterceptor: TokenInterceptor,
    private popupService: PopupService,
    private userStateService: UseStateService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.sidebarStatusService.status$.subscribe(status => {
      this.isActiveMenuHeader = status;
    });
  }

  logout(): void {
    this.tokenInterceptor.clearTokens();
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
    // Lógica para cerrar sidebar si es necesario
    console.log('Cerrando sidebar');
  }
}