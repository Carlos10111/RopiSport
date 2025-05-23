import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pago } from '../../models/pago';
import { PagoDTO } from '../../dtos/pago-dto';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PagoService {
  private apiUrl = `${environment.apiUrl}/pagos`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Pago[]> {
    return this.http.get<Pago[]>(this.apiUrl);
  }

  getById(id: number): Observable<Pago> {
    return this.http.get<Pago>(`${this.apiUrl}/${id}`);
  }

  create(pagoDTO: PagoDTO): Observable<Pago> {
    return this.http.post<Pago>(this.apiUrl, pagoDTO);
  }

  update(id: number, pagoDTO: PagoDTO): Observable<Pago> {
    return this.http.put<Pago>(`${this.apiUrl}/${id}`, pagoDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
