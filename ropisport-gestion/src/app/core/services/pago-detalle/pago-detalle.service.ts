import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PagoDetalle } from '../../models/pago-detalle';
import { PagoDetalleDTO } from '../../dtos/pago-detalle-dto';

@Injectable({
  providedIn: 'root'
})
export class PagoDetalleService {
  private apiUrl = `${environment.apiUrl}/pagos-detalle`;

  constructor(private http: HttpClient) { }

  // Obtener detalles por ID de pago
  getDetallesByPagoId(pagoId: number): Observable<PagoDetalle[]> {
    return this.http.get<PagoDetalle[]>(`${this.apiUrl}/pago/${pagoId}`);
  }

  // Obtener detalle por ID
  getDetalleById(id: number): Observable<PagoDetalle> {
    return this.http.get<PagoDetalle>(`${this.apiUrl}/${id}`);
  }

  // Crear nuevo detalle
  createDetalle(detalleDTO: PagoDetalleDTO): Observable<PagoDetalle> {
    return this.http.post<PagoDetalle>(this.apiUrl, detalleDTO);
  }

  // Actualizar detalle existente
  updateDetalle(id: number, detalleDTO: PagoDetalleDTO): Observable<PagoDetalle> {
    return this.http.put<PagoDetalle>(`${this.apiUrl}/${id}`, detalleDTO);
  }

  // Eliminar detalle
  deleteDetalle(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}