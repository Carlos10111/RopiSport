import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Socia } from '../../models/socia';
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

  create(socia: Socia): Observable<Socia> {
    return this.http.post<Socia>(this.apiUrl, socia);
  }

  update(id: number, socia: Socia): Observable<Socia> {
    return this.http.put<Socia>(`${this.apiUrl}/${id}`, socia);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
