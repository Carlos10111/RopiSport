import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagoDetalle } from '../../models/pago-detalle';
import { PagoDetalleDTO } from '../../dtos/pago-detalle-dto';
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

  create(pagoDetalleDTO: PagoDetalleDTO): Observable<PagoDetalle> {
    return this.http.post<PagoDetalle>(this.apiUrl, pagoDetalleDTO);
  }

  update(id: number, pagoDetalleDTO: PagoDetalleDTO): Observable<PagoDetalle> {
    return this.http.put<PagoDetalle>(`${this.apiUrl}/${id}`, pagoDetalleDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
