import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../../models/categoria';
import { CategoriaDTO } from '../../dtos/categoria-negocio-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private apiUrl = `${environment.apiUrl}/categorias`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

  getById(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.apiUrl}/${id}`);
  }

  create(categoriaDTO: CategoriaDTO): Observable<Categoria> {
    return this.http.post<Categoria>(this.apiUrl, categoriaDTO);
  }

  update(id: number, categoriaDTO: CategoriaDTO): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.apiUrl}/${id}`, categoriaDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
