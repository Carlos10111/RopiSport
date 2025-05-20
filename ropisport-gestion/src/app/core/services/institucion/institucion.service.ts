import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Institucion } from '../../models/institucion';
import { InstitucionDTO } from '../../dtos/institucion-dto';
import { environment } from '../../../../environments/environment';
import { PaginatedResponse } from '../../models/paginated-response';
import { TipoInstitucion } from '../../models/tipo-institucion';

@Injectable({ providedIn: 'root' })
export class InstitucionService {
  private apiUrl = `${environment.apiUrl}/instituciones`;

  constructor(private http: HttpClient) {}

  getAllInstituciones(page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<PaginatedResponse<Institucion>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<PaginatedResponse<Institucion>>(this.apiUrl, { params });
  }
  /*getAllInstituciones(page: number, size: number): Observable<PaginatedResponse<Institucion>> {
    return this.http.get<PaginatedResponse<Institucion>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  searchInstituciones(term: string, page: number, size: number): Observable<PaginatedResponse<Institucion>> {
    return this.http.get<PaginatedResponse<Institucion>>(`${this.apiUrl}/search?term=${term}&page=${page}&size=${size}`);
  }*/
  searchInstituciones(texto: string, page: number = 0, size: number = 10): Observable<PaginatedResponse<Institucion>> {
    const params = new HttpParams()
      .set('texto', texto)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PaginatedResponse<Institucion>>(`${this.apiUrl}/buscar`, { params });
  }

  getInstitucionById(id: number): Observable<Institucion> {
    return this.http.get<Institucion>(`${this.apiUrl}/${id}`);
  }

  createInstitucion(institucionDTO: InstitucionDTO): Observable<Institucion> {
    return this.http.post<Institucion>(this.apiUrl, institucionDTO);
  }

  updateInstitucion(id: number, institucionDTO: InstitucionDTO | Institucion): Observable<Institucion> {
    return this.http.put<Institucion>(`${this.apiUrl}/${id}`, institucionDTO);
  }

  deleteInstitucion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTiposInstitucion(): Observable<TipoInstitucion[]> {
    return this.http.get<TipoInstitucion[]>(`${this.apiUrl}/tipos`);
  }
}



/*import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Institucion } from '../../models/institucion';
import { InstitucionDTO } from '../../dtos/institucion-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class InstitucionService {
  private apiUrl = `${environment.apiUrl}/instituciones`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Institucion[]> {
    return this.http.get<Institucion[]>(this.apiUrl);
  }

  getById(id: number): Observable<Institucion> {
    return this.http.get<Institucion>(`${this.apiUrl}/${id}`);
  }

  create(institucionDTO: InstitucionDTO): Observable<Institucion> {
    return this.http.post<Institucion>(this.apiUrl, institucionDTO);
  }

  update(id: number, institucionDTO: InstitucionDTO): Observable<Institucion> {
    return this.http.put<Institucion>(`${this.apiUrl}/${id}`, institucionDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}*/
