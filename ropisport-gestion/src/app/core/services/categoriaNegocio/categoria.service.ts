import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoriaNegocio } from '../../models/categoriaNegocio';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private apiUrl = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<CategoriaNegocio[]> {
    return this.http.get<CategoriaNegocio[]>(this.apiUrl);
  }

  getById(id: number): Observable<CategoriaNegocio> {
    return this.http.get<CategoriaNegocio>(`${this.apiUrl}/${id}`);
  }

  create(categoria: CategoriaNegocio): Observable<CategoriaNegocio> {
    return this.http.post<CategoriaNegocio>(this.apiUrl, categoria);
  }

  update(id: number, categoria: CategoriaNegocio): Observable<CategoriaNegocio> {
    return this.http.put<CategoriaNegocio>(`${this.apiUrl}/${id}`, categoria);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
