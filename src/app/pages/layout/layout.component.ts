import { Component, OnInit } from '@angular/core';
 import { HeaderComponent } from '../header/header.component';
import { RouterOutlet } from '@angular/router';
import {SidebarComponent} from '../sidebar/sidebar.component';
import {SidebarStatusService} from '../../services/status/sidebar-status.service';


@Component({
  selector: 'app-layout',
  imports: [
    HeaderComponent,
    RouterOutlet,
    SidebarComponent
  ],
  standalone: true,
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
  isActiveSidebar: boolean = true;

  constructor(
    private sidebarStatusService: SidebarStatusService,
  )
  {}

  ngOnInit():void {
    this.sidebarStatusService.status$.subscribe(valorProcedenteDelHeader => {
      this.isActiveSidebar = valorProcedenteDelHeader;
    })
  }
}
