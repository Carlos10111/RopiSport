// app.component.ts
import { Component } from '@angular/core';
import { Router, RouterModule,RouterOutlet, NavigationEnd } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { filter } from 'rxjs/operators';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  isAdminRoute= false;
  title= "Amea"
  constructor(
    private router :Router,
    private titleService:Title

  ){}
  ngOnInit() {
    // Establecer el título por defecto
    this.titleService.setTitle(this.title);
    
    // Detectar cuando estamos en una ruta de administración
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isAdminRoute = event.url.includes('/admin');
      
      // Cambiar título según la ruta
      if (this.isAdminRoute) {
        this.titleService.setTitle('Panel de Administración - Mueblería');
      } else {
        this.titleService.setTitle(this.title);
      }
    });
  }
}
