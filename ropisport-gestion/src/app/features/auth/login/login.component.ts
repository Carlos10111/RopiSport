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
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
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
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

// En login.component.ts
submit() {
  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    return;
  }

  this.popupService.loader("Cargando...", "Espere un momento");

  this.credentialsService.login(this.loginForm.value as LoginRequest).subscribe({
    next: (data) => {
      console.log('Respuesta del login:', data);
      
      // Cambiar saveTokens por setToken
      this.tokenService.setToken(data.token);
      this.useStateService.save(data.username, data.rol);
      
      // Navegar al panel de administración
      this.router.navigate(['/admin']).then(() => {
        this.popupService.close();
      });
    },
    error: err => {
      console.log("Error en login:", err);
      let mensaje = "Ha ocurrido un error.";
      
      if (err.error === "Invalid password") {
        mensaje = "Contraseña incorrecta, inténtelo de nuevo.";
      } else if (err.error === "User not found") {
        mensaje = "El usuario no existe. Compruebe los datos o regístrese en la plataforma.";
      }
      
      this.popupService.close();
      this.popupService.showMessage('Error', mensaje, 'error');
    }
  });
}
}