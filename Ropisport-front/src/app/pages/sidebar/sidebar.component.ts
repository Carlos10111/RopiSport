import {Component, OnInit} from '@angular/core';
import {SidebarStatusService} from '../../services/status/sidebar-status.service';
import {UseStateService} from '../../services/auth/use-state.service';
import {PopupService} from '../../services/utils/popup.service';
import {TokenService} from '../../services/auth/token.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [],
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
  goToProfile() {
  this.router.navigate(['/app/perfil']);
}
goToProductos() {
  this.router.navigate(['/tienda']);
}
goToInicio() {
  this.router.navigate(['/']);
}

}



