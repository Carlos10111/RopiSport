import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoInstitucion } from '../../models/tipo-institucion';
import { TipoInstitucionDTO } from '../../dtos/tipo-institucion-dto';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class TipoInstitucionService {
  private apiUrl = `${environment.apiUrl}/tipos-institucion`;

  constructor(private http: HttpClient) {}

  getAllTiposInstitucion(): Observable<TipoInstitucion[]> {
    return this.http.get<TipoInstitucion[]>(this.apiUrl);
  }

  getTipoInstitucionById(id: number): Observable<TipoInstitucion> {
    return this.http.get<TipoInstitucion>(`${this.apiUrl}/${id}`);
  }

  createTipoInstitucion(tipoRequest: TipoInstitucionDTO): Observable<TipoInstitucion> {
    return this.http.post<TipoInstitucion>(this.apiUrl, tipoRequest);
  }

  updateTipoInstitucion(id: number, tipoRequest: TipoInstitucionDTO): Observable<TipoInstitucion> {
    return this.http.put<TipoInstitucion>(`${this.apiUrl}/${id}`, tipoRequest);
  }

  deleteTipoInstitucion(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  searchTiposInstitucion(nombre: string): Observable<TipoInstitucion[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<TipoInstitucion[]>(`${this.apiUrl}/search`, { params });
  }
}