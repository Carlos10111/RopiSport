import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse } from '../../core/models/auth';
import { Usuario } from '../../core/models/usuario';

@Injectable({
  providedIn: 'root'
})
export class CredentialsService {

  constructor(
    private http: HttpClient,
  ) { }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/users/login`, credentials)
  }


  register(userData: Usuario): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/users/register`, userData)
  }

  changePassword(username: string, payload: { username: string; currentPassword: string; newPassword: string; }, newPassword: any) {
    return this.http.post<{ message: string }>('/api/auth/change-password', payload);
  }
  

}

/*import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, AuthResponse, RegisterRequest } from '../../core/models/auth';
import { Usuario } from '../../core/models/usuario';

@Injectable({
  providedIn: 'root'
})
export class CredentialsService {

  constructor(
    private http: HttpClient,
  ) { }

  login(credentials: LoginRequest): Observable</*any*//*AuthResponse> {
    return this.http.post<any>(`${environment.apiUrl}/users/login`, credentials)
  }

  register(userData: RegisterRequest): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/users/register`, userData)
  }

  changePassword(username: string, payload: { username: string; currentPassword: string; newPassword: string; }, newPassword: any) {
    return this.http.post<{ message: string }>('/api/auth/change-password', payload);
  }
  

}*/
