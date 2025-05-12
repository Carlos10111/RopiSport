import { Component } from '@angular/core';
import {SidebarStatusService} from '../../services/status/sidebar-status.service';
import { Router } from '@angular/router';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  isActive: boolean = false;

  // Variables de tabs

  isActiveItems: any = {
    isActiveNotification: false,
    isActiveSettings: false,
  }

  constructor(
    private sidebarStatusService: SidebarStatusService,
    private router: Router
  ) {}

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  // Método para redirigir a la página de Register
  goToRegister(): void {
    this.router.navigate(['/registro']);
  }
  // isActiveNotification: boolean = false;

  /*toggleLogo() {
    this.isActive = !this.isActive;
    this.sidebarStatusService.changeStatus(this.isActive);
  }
  goToProfile() {
    this.router.navigate(['/app/perfil']);
  }
  toggleItem(option: string) {
    if (this.isActiveItems[option]) {
      this.isActiveItems[option] = false;
    }
    else {
      Object.keys(this.isActiveItems).forEach((item) => {
        this.isActiveItems[item] = false;
      })
      this.isActiveItems[option] = true;
    }
  }*/

}
