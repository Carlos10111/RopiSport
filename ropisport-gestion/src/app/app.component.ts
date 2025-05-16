// app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'ropisport-gestion';
  // Aquí puedes agregar más propiedades o métodos según sea necesario
  // Por ejemplo, para manejar la navegación o el estado de la aplicación
  // constructor(private router: Router) {}
  // navigateTo(route: string) {
  //   this.router.navigate([route]);
  // }
}
