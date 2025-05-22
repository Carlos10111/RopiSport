import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import { CredentialsService } from '../../../core/auth/credentials.service';
import { LoginInterface, /*RegisterInterface,*/ } from '../../../core/models/auth';
import { Usuario } from '../../../core/models/usuario';
import { Router } from '@angular/router';
import { PopupService } from '../../../shared/utils/popup.service';
import { UseStateService } from '../../../core/auth/use-state.service';
import { TokenService } from '../../../core/auth/token.service';



@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private credentialsService: CredentialsService,
    private router: Router,
    private popupService: PopupService,
    private userStateService: UseStateService,
    private tokenService : TokenService

  )

  {
    this.loginForm = this.formBuilder.group ({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    })
  }

  submit() {
    if (this.loginForm.invalid) {
      return;
    }

    
    this.credentialsService.login(this.loginForm.value as LoginInterface).subscribe({
      next: (data) => {
        console.log('Token recibido:', data.token);
        console.log('Datos completos:', data);
        this.popupService.loader("Iniciado sesion...", "Espere un momento");
        setTimeout(() => {

          this.tokenService.saveTokens(data.token, ""/*"234325423423"*/)
          //this.userStateService.save(data.username);
          this.userStateService.save(data.username, data.rol);
          //sessionStorage.setItem('username', data.username);
          this.popupService.close();
          this.router.navigate(['/']);
          
        }, 1500)
      },

      error: err => {
        let message;
        if (err.error == "Invalid password") {
          message = "Contraseña incorrecta, inténtelo de nuevo."
        }
        else if (err.error == "User not found") {
          message = "El usuario no existe. Compruebe los datos o registrate en la plataforma"
        }
        else {
          message = err.error;
        }

        this.popupService.showMessage(
          'Ups ha ocurrido un error', message, 'error'
        );
      }
    })
      
  }

  /*goToRegister(): void {
    this.router.navigate(['/auth/register']);
  }*/
}




/*import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CredentialsService } from '../../../core/auth/credentials.service';
import { LoginRequest } from '../../../core/models/auth';
import { TokenService } from '../../../core/auth/token.service';
import { Router } from '@angular/router';
import { UseStateService } from '../../../core/auth/use-state.service';
import { PopupService } from '../../../shared/utils/popup.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
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
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  submit() {
    if (this.loginForm.invalid) return;

    const credentials: LoginRequest = this.loginForm.value;

    console.log('Intentando login con:', credentials.username, credentials.password);

    this.popupService.loader("Cargando...", "Espere un momento");

    this.credentialsService.login(credentials).subscribe({
      next: (data) => {
        // Guardamos token en cookies
        this.tokenService.saveTokens(data.token/*, ''*//*);

        // Guardamos el usuario en sesión
        this.useStateService.save(data.username, data.rol);

        // Redireccionamos al home (o donde sea)
        this.router.navigate(['']).then(() => this.popupService.close());
      },
      error: (err) => {
        console.error("Error en login:", err);

        let message = "Ha ocurrido un error al iniciar sesión.";
        if (err.status === 401) {
          message = "Credenciales incorrectas. Por favor, revísalas e inténtalo de nuevo.";
        } else if (err.error?.message) {
          message = err.error.message;
        }

        this.popupService.close();
        this.popupService.showMessage("Error", message, "error");
      }
    });
  }
}
*/