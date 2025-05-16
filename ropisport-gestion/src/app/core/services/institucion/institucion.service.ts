import { Injectable } from '@angular/core';
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
}
