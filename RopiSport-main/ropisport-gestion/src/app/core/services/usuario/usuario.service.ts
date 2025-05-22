import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../../models/usuario';
import { UsuarioDTO } from '../../dtos/usuario-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  create(usuarioDTO: UsuarioDTO): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuarioDTO);
  }

  update(id: number, usuarioDTO: UsuarioDTO): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuarioDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
