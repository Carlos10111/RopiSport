import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CredentialsService } from '../../services/auth/credentials.service';
import { LoginInterface } from '../../services/interfaces/auth';
import { TokenService } from '../../services/auth/token.service';
import { Router } from '@angular/router';
import { UseStateService } from '../../services/auth/use-state.service';
import { PopupService } from '../../services/utils/popup.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
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

  submit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.popupService.loader("Cargando...", "Espere un momento");

    this.credentialsService.login(this.loginForm.value as LoginInterface).subscribe({
      next: (data) => {
        this.tokenService.saveTokens(data.token, '');
        this.useStateService.save(data.username, data.role);

        this.router.navigate(['/app/control-panel']).then(() => {
          this.popupService.close();
        });
      },
      error: err => {
        console.log("Error en login:", err);
        let message = "Ha ocurrido un error.";
        if (err.error === "Invalid password") {
          message = "Contraseña incorrecta, inténtelo de nuevo.";
        } else if (err.error === "User not found") {
          message = "El usuario no existe. Compruebe los datos o regístrese en la plataforma.";
        }

        this.popupService.close();
        this.popupService.showMessage('Error', message, 'error');
      }
    });
  }
}
