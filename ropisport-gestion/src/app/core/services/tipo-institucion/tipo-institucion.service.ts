import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoInstitucion } from '../../models/tipo-institucion';
import { TipoInstitucionDTO } from '../../dtos/tipo-institucion-dto';
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

  create(tipoDTO: TipoInstitucionDTO): Observable<TipoInstitucion> {
    return this.http.post<TipoInstitucion>(this.apiUrl, tipoDTO);
  }

  update(id: number, tipoDTO: TipoInstitucionDTO): Observable<TipoInstitucion> {
    return this.http.put<TipoInstitucion>(`${this.apiUrl}/${id}`, tipoDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
