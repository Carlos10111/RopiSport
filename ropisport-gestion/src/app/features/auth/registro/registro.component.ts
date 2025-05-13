import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { CredentialsService } from '../../../core/auth/credentials.service';
import { Usuario } from '../../../core/models/usuario';
import { Router } from '@angular/router';
import { PopupService } from '../../../shared/utils/popup.service';

@Component({
  selector: 'app-registro',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.scss'
})
export class RegistroComponent {
  registerForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private credentialsService: CredentialsService,
    private router: Router,
    private popupService: PopupService
  ) {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      roleName: ['', [Validators.required]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      address: ['', [Validators.required]],
    });
  }

  submit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.credentialsService.register(this.registerForm.value as Usuario).subscribe({
      next: () => {
        this.popupService.showMessage('Registro exitoso', 'Ahora puedes iniciar sesión.', 'success');
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
      },
      error: err => {
        this.popupService.showMessage(
          'Error en el registro', 
          err.error || 'Ha ocurrido un problema, intenta nuevamente.', 
          'error'
        );
      }
    });
  }
}
