import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CredentialsService } from '../../../core/auth/credentials.service';
import { LoginRequest } from '../../../core/models/auth';
import { TokenService } from '../../../core/auth/token.service';
import { Router, RouterModule } from '@angular/router';
import { UseStateService } from '../../../core/auth/use-state.service';
import { PopupService } from '../../../shared/utils/popup.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule, RouterModule,],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private credentialsService: CredentialsService,
    private tokenService: TokenService,
    private router: Router,
    private useStateService: UseStateService,
    private popupService: PopupService
  ) {
    // Construir el formulario con validaciones
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  submit() {
    // Si el formulario es inválido, marcar todos los campos como tocados para mostrar errores
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    // Mostrar popup de carga mientras se procesa la solicitud
    this.popupService.loader("Cargando...", "Espere un momento");

    // Ejecutar el login con los datos del formulario
    this.credentialsService.login(this.loginForm.value as LoginRequest).subscribe({
      next: (data) => {
          console.log('Respuesta del login:', data);
        // Guardar tokens y estado de usuario
        this.tokenService.saveTokens(data.token, '');
this.useStateService.save(data.username, data.rol);

        // Navegar al panel de control
        this.router.navigate(['/app/control-panel']).then(() => {
          // Cerrar popup de carga
          this.popupService.close();
        });
      },
      error: err => {
        console.log("Error en login:", err);
        let mensaje = "Ha ocurrido un error.";

        // Personalizar mensajes de error según respuesta
        if (err.error === "Invalid password") {
          mensaje = "Contraseña incorrecta, inténtelo de nuevo.";
        } else if (err.error === "User not found") {
          mensaje = "El usuario no existe. Compruebe los datos o regístrese en la plataforma.";
        }

        // Cerrar popup de carga y mostrar mensaje de error
        this.popupService.close();
        this.popupService.showMessage('Error', mensaje, 'error');
      }
    });
  }
}
