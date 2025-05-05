import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Perfil } from '../interfaces/perfil';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private apiUrl = `${environment.apiUrl}/perfil`;

  constructor(private http: HttpClient) {}

  getPerfil(): Observable<Perfil> {
    return this.http.get<Perfil>(this.apiUrl);
  }

  actualizarPerfil(perfil: Perfil): Observable<Perfil> {
    return this.http.put<Perfil>(this.apiUrl, perfil);
  }

  cambiarPassword(nuevaPassword: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/cambiar-password`, { password: nuevaPassword });
  }
}
