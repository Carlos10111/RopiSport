import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Empresa } from '../../models/empresa';
import { EmpresaDTO } from '../../dtos/empresa-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class EmpresaService {
  private apiUrl = `${environment.apiUrl}/empresas`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(this.apiUrl);
  }

  getById(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.apiUrl}/${id}`);
  }

  create(empresaDTO: EmpresaDTO): Observable<Empresa> {
    return this.http.post<Empresa>(this.apiUrl, empresaDTO);
  }

  update(id: number, empresaDTO: EmpresaDTO): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.apiUrl}/${id}`, empresaDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
