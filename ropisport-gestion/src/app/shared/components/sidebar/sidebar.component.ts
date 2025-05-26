import { Component, OnInit } from '@angular/core';
import { SidebarStatusService } from '../../status/sidebar-status.service';
import { UseStateService } from '../../../core/auth/use-state.service';
import { PopupService } from '../../utils/popup.service';
// ❌ ELIMINAR esta línea (no necesitas TokenInterceptor aquí)
// import { TokenInterceptor } from '../../../core/interceptors/token.interceptor';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from './../../../core/auth/token.service';

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
    // ❌ ELIMINAR esta línea
    // private tokenInterceptor: TokenInterceptor,
    
    // ✅ AÑADIR esta línea
    private tokenService: TokenService,
    
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
    // ✅ CORREGIR esta línea
    this.tokenService.clearAll(); // Era: this.TokenService.clearAll();
    
    this.userStateService.clear();
    this.isAuthenticated = false;
    this.currentUser = null;
    this.router.navigate(['/login']);
  }

  closeSidebar(): void {
    console.log('Cerrando sidebar');
  }
}