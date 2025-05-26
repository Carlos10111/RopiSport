import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Pago } from '../../models/pago';
import { PagoDTO } from '../../dtos/pago-dto';
import { PaginatedResponse } from '../../dtos/paginated-response';

@Injectable({
  providedIn: 'root'
})
export class PagoService {
  private apiUrl = `${environment.apiUrl}/pagos`;

  constructor(private http: HttpClient) { }

  // Obtener todos los pagos con paginación
  getAllPagos(page: number = 0, size: number = 10, sortBy: string = 'fechaPago'): Observable<PaginatedResponse<Pago>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy);

    return this.http.get<PaginatedResponse<Pago>>(this.apiUrl, { params });
  }

  // Obtener pago por ID
  getPagoById(id: number): Observable<Pago> {
    return this.http.get<Pago>(`${this.apiUrl}/${id}`);
  }

  // Obtener pagos por socia ID
  getPagosBySociaId(sociaId: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/socia/${sociaId}`);
  }

  // Obtener pagos por rango de fechas
  getPagosByFecha(inicio: string, fin: string): Observable<Pago[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);

    return this.http.get<Pago[]>(`${this.apiUrl}/fecha`, { params });
  }

  // Obtener pagos confirmados o pendientes
  getPagosByConfirmado(confirmado: boolean): Observable<Pago[]> {
    const params = new HttpParams().set('confirmado', confirmado.toString());
    return this.http.get<Pago[]>(`${this.apiUrl}/confirmados`, { params });
  }

  // Buscar pagos (similar al patrón de instituciones)
  searchPagos(termino: string): Observable<Pago[]> {
    // Implementamos una búsqueda que combine varios criterios
    // Podrías necesitar crear este endpoint en el backend o usar filtros existentes
    const params = new HttpParams().set('search', termino);
    return this.http.get<Pago[]>(`${this.apiUrl}/search`, { params });
  }

  // Crear nuevo pago
  createPago(pagoDTO: PagoDTO): Observable<Pago> {
    return this.http.post<Pago>(this.apiUrl, pagoDTO);
  }

  // Actualizar pago existente
  updatePago(id: number, pagoDTO: PagoDTO): Observable<Pago> {
    return this.http.put<Pago>(`${this.apiUrl}/${id}`, pagoDTO);
  }

  // Eliminar pago
  deletePago(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // Exportar a Excel
  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/excel`, { 
      responseType: 'blob'
    });
  }
}