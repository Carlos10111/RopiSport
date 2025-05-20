import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse } from '../../core/models/auth';
import { Usuario } from '../../core/models/usuario';
import { TokenService } from '../auth/token.service';

@Injectable({
  providedIn: 'root'
})
export class CredentialsService {

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }
  
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      // para almacenar los tokens tras iniciar sesión:
      tap((response: AuthResponse) => {
        this.tokenService.saveTokens(response.token/*, response.refreshToken*/);
      })
    );
  }


  register(userData: Usuario): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, userData)
  }

  changePassword(username: string, payload: { username: string; currentPassword: string; newPassword: string; }, newPassword: any) {
    return this.http.post<{ message: string }>('/auth/change-password', payload);
  }
  

}
