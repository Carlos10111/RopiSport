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

  submit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
   
    this.popupService.loader("Cargando...", "Espere un momento");
   
    this.credentialsService.login(this.loginForm.value as LoginRequest).subscribe({
      next: (data) => {
        console.log('✅ Respuesta del login:', data);
        console.log('🔑 Token recibido:', data.token?.substring(0, 30) + '...');
        
        // 🔍 DECODIFICAR EL JWT PARA DEBUG
        try {
          const payload = JSON.parse(atob(data.token.split('.')[1]));
          console.log('🔍 CONTENIDO COMPLETO DEL JWT:', payload);
          console.log('👤 Subject (usuario):', payload.sub);
          console.log('🏷️ Authorities en JWT:', payload.authorities);
          console.log('🏷️ Role en JWT:', payload.role);
          console.log('🏷️ Roles en JWT:', payload.roles);
          console.log('⏰ Expira:', new Date(payload.exp * 1000));
        } catch (error) {
          console.error('❌ Error decodificando JWT:', error);
        }
       
        // Guardar token
        this.tokenService.setToken(data.token);
        this.useStateService.save(data.username, data.rol);
       
        // VERIFICAR que se guardó correctamente
        console.log('💾 Token en localStorage después del login:', localStorage.getItem('auth_token')?.substring(0, 30) + '...');
        console.log('🔍 Token desde servicio después del login:', this.tokenService.getToken()?.substring(0, 30) + '...');
        console.log('👤 ¿Usuario autenticado?:', this.tokenService.isAuthenticated());
       
        // Navegar al panel de administración
        this.router.navigate(['/admin']).then(() => {
          this.popupService.close();
        });
      },
      error: err => {
        console.log("❌ Error en login:", err);
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