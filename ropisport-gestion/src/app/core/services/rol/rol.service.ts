import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rol } from '../../models/rol';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RolService {
  private apiUrl = `${environment.apiUrl}/roles`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Rol[]> {
    return this.http.get<Rol[]>(this.apiUrl);
  }

  getById(id: number): Observable<Rol> {
    return this.http.get<Rol>(`${this.apiUrl}/${id}`);
  }

  create(rol: Rol): Observable<Rol> {
    return this.http.post<Rol>(this.apiUrl, rol);
  }

  update(id: number, rol: Rol): Observable<Rol> {
    return this.http.put<Rol>(`${this.apiUrl}/${id}`, rol);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}