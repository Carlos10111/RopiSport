import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagoDetalle } from '../../models/pago-detalle';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PagoDetalleService {
  private apiUrl = `${environment.apiUrl}/pago-detalles`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<PagoDetalle[]> {
    return this.http.get<PagoDetalle[]>(this.apiUrl);
  }

  getById(id: number): Observable<PagoDetalle> {
    return this.http.get<PagoDetalle>(`${this.apiUrl}/${id}`);
  }

  create(pagoDetalle: PagoDetalle): Observable<PagoDetalle> {
    return this.http.post<PagoDetalle>(this.apiUrl, pagoDetalle);
  }

  update(id: number, pagoDetalle: PagoDetalle): Observable<PagoDetalle> {
    return this.http.put<PagoDetalle>(`${this.apiUrl}/${id}`, pagoDetalle);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}