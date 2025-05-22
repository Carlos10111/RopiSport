import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginInterface/*, RegisterInterface*/ } from '../models/auth';

@Injectable({
  providedIn: 'root'
})
export class CredentialsService {

  constructor(
    private http: HttpClient,
  ) { }

  login(credentials: LoginInterface): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, credentials)
  }


  /*register(userData: RegisterInterface): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/register`, userData)
  }*/

  /*changePassword(username: string, payload: { username: string; currentPassword: string; newPassword: string; }, newPassword: any) {
    return this.http.post<{ message: string }>('/api/auth/change-password', payload);
  }*/
  
}
