import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class SociaService {
  private apiUrl = `${environment.apiUrl}/socias`;

  constructor(private http: HttpClient) { }

  getAllSocias(page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    
    return this.http.get(this.apiUrl, { params });
  }

  searchSocias(texto: string, page: number = 0, size: number = 10): Observable<any> {
    let params = new HttpParams()
      .set('texto', texto)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get(`${this.apiUrl}/buscar`, { params });
  }

  getSociaById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  createSocia(socia: any): Observable<any> {
    return this.http.post(this.apiUrl, socia);
  }

  updateSocia(id: number, socia: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, socia);
  }

  deleteSocia(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  cambiarEstado(id: number, activa: boolean, observaciones?: string): Observable<any> {
    let params = new HttpParams()
      .set('activa', activa.toString());
    
    if (observaciones) {
      params = params.set('observaciones', observaciones);
    }
    
    return this.http.patch(`${this.apiUrl}/${id}/estado`, {}, { params });
  }
}