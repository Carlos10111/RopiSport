import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Institucion } from '../../models/institucion';
import { InstitucionDTO } from '../../dtos/institucion-dto';
import { PaginatedResponse } from '../../models/paginated-response';
import { ApiResponse } from '../../models/api-response';
import { SearchRequest } from '../../dtos/search-request';

@Injectable({
  providedIn: 'root'
})
export class InstitucionService {
  private apiUrl = `${environment.apiUrl}/instituciones`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene headers con autenticación si está disponible
   */
  private getAuthHeaders(): HttpHeaders {
    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    // Si tienes un token de autenticación, agrégalo aquí
    const token = localStorage.getItem('authToken') || sessionStorage.getItem('authToken');
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  getAllInstituciones(page: number = 0, size: number = 10, sortBy: string = 'id'): Observable<PaginatedResponse<Institucion>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy); // Cambiado de 'sort' a 'sortBy'

    return this.http.get<PaginatedResponse<Institucion>>(this.apiUrl, { 
      params,
      headers: this.getAuthHeaders()
    });
  }

  searchInstituciones(
    texto: string,
    tipoInstitucionId?: number,
    page: number = 0,
    size: number = 10,
    sort: string = 'id,desc'
  ): Observable<PaginatedResponse<Institucion>> {
    let params = new HttpParams()
      .set('texto', texto)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
  
    if (tipoInstitucionId !== undefined && tipoInstitucionId !== null) {
      params = params.set('tipoInstitucionId', tipoInstitucionId.toString());
    }
  
    return this.http.get<PaginatedResponse<Institucion>>(`${this.apiUrl}/buscar`, {
      headers: this.getAuthHeaders(),
      params
    });
  }
  

  getInstitucionById(id: number): Observable<Institucion> {
    return this.http.get<Institucion>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  getInstitucionesByTipoId(tipoId: number): Observable<Institucion[]> {
    return this.http.get<Institucion[]>(`${this.apiUrl}/tipo/${tipoId}`, {
      headers: this.getAuthHeaders()
    });
  }

  createInstitucion(institucionDTO: InstitucionDTO): Observable<Institucion> {
    return this.http.post<Institucion>(this.apiUrl, institucionDTO, {
      headers: this.getAuthHeaders()
    });
  }

  updateInstitucion(id: number, institucionDTO: InstitucionDTO): Observable<Institucion> {
    return this.http.put<Institucion>(`${this.apiUrl}/${id}`, institucionDTO, {
      headers: this.getAuthHeaders()
    });
  }

  deleteInstitucion(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/excel`, {
      responseType: 'blob',
      headers: this.getAuthHeaders()
    });
  }
}