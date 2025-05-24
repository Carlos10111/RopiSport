import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Socia } from '../../models/socia';
import { SociaDTO } from '../../dtos/socia-dto';
import { PaginatedResponse } from '../../dtos/paginated-response';

@Injectable({
  providedIn: 'root'
})
export class SociaService {
  private apiUrl = `${environment.apiUrl}/socias`;

  constructor(private http: HttpClient) {}

  getAllSocias(page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<PaginatedResponse<Socia>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<PaginatedResponse<Socia>>(this.apiUrl, { params });
  }

  searchSocias(texto: string, page: number = 0, size: number = 10): Observable<PaginatedResponse<Socia>> {
    const params = new HttpParams()
      .set('texto', texto)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PaginatedResponse<Socia>>(`${this.apiUrl}/buscar`, { params });
  }

  getSociaById(id: number): Observable<Socia> {
    return this.http.get<Socia>(`${this.apiUrl}/${id}`);
  }

  createSocia(sociaDTO: SociaDTO): Observable<Socia> {
    return this.http.post<Socia>(this.apiUrl, sociaDTO);
  }

  updateSocia(id: number, sociaDTO: SociaDTO): Observable<Socia> {
    return this.http.put<Socia>(`${this.apiUrl}/${id}`, sociaDTO);
  }

  deleteSocia(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  cambiarEstado(id: number, activa: boolean, observaciones?: string): Observable<Socia> {
    let params = new HttpParams().set('activa', activa.toString());

    if (observaciones) {
      params = params.set('observaciones', observaciones);
    }

    return this.http.patch<Socia>(`${this.apiUrl}/${id}/estado`, {}, { params });
  }
}




/*import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Socia } from '../../models/socia';
import { SociaDTO } from '../../dtos/socia-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SociaService {
  private apiUrl = `${environment.apiUrl}/socias`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Socia[]> {
    return this.http.get<Socia[]>(this.apiUrl);
  }

  getById(id: number): Observable<Socia> {
    return this.http.get<Socia>(`${this.apiUrl}/${id}`);
  }

  create(sociaDTO: SociaDTO): Observable<Socia> {
    return this.http.post<Socia>(this.apiUrl, sociaDTO);
  }

  update(id: number, sociaDTO: SociaDTO): Observable<Socia> {
    return this.http.put<Socia>(`${this.apiUrl}/${id}`, sociaDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
*/