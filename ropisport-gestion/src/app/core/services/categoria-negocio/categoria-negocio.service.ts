import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoriaNegocio } from '../../models/categoriaNegocio';
import { CategoriaDTO } from '../../dtos/categoria-negocio-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CategoriaNegocioService {
  
  private apiUrl = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<CategoriaNegocio[]> {
    return this.http.get<CategoriaNegocio[]>(this.apiUrl);
  }


  getById(id: number): Observable<CategoriaNegocio> {
    return this.http.get<CategoriaNegocio>(`${this.apiUrl}/${id}`);
  }

  create(categoriaDTO: CategoriaDTO): Observable<CategoriaNegocio> {
    return this.http.post<CategoriaNegocio>(this.apiUrl, categoriaDTO);
  }

  update(id: number, categoriaDTO: CategoriaDTO): Observable<CategoriaNegocio> {
    return this.http.put<CategoriaNegocio>(`${this.apiUrl}/${id}`, categoriaDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
