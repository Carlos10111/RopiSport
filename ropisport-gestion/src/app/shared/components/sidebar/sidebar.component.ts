import {Component, OnInit} from '@angular/core';
import { SidebarStatusService } from '../../status/sidebar-status.service';
import { UseStateService } from '../../../core/auth/use-state.service';
import { PopupService } from '../../utils/popup.service';
import { TokenService } from '../../../core/auth/token.service';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [],
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {

  isActiveMenuHeader: boolean = true;
  constructor(
    private sidebarStatusService: SidebarStatusService,
    private tokenService: TokenService,
    private popupService: PopupService,
    private userStateService: UseStateService,
    private router: Router,
  )
  {}

  ngOnInit(): void {
    this.sidebarStatusService.status$.subscribe(status => {
      this.isActiveMenuHeader = status;
    })
  }

  closeSession(): void {
    this.popupService.loader(
      "Cerrando sesión",
      "Vuelva pronto"
    );

    this.tokenService.removeToken();
    this.userStateService.removeSession()
    setTimeout(() => {
      this.popupService.close()
      this.router.navigate(['/login']);
    }, 1500)
  }
goToInicio() {
  this.router.navigate(['/']);
}

  irASocias() {
    this.router.navigate(['/socias']);
  }

  irAProveedores() {
    this.router.navigate(['/proveedores']);
  }
}



