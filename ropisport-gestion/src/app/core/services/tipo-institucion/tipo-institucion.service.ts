import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoInstitucion } from '../../models/tipo-institucion';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TipoInstitucionService {
  private apiUrl = `${environment.apiUrl}/tipos-institucion`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<TipoInstitucion[]> {
    return this.http.get<TipoInstitucion[]>(this.apiUrl);
  }

  getById(id: number): Observable<TipoInstitucion> {
    return this.http.get<TipoInstitucion>(`${this.apiUrl}/${id}`);
  }

  create(tipo: TipoInstitucion): Observable<TipoInstitucion> {
    return this.http.post<TipoInstitucion>(this.apiUrl, tipo);
  }

  update(id: number, tipo: TipoInstitucion): Observable<TipoInstitucion> {
    return this.http.put<TipoInstitucion>(`${this.apiUrl}/${id}`, tipo);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}