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
 
  /**
   * Obtiene todas las socias con paginación y filtros opcionales
   * @param page Número de página (0 por defecto)
   * @param size Tamaño de página (10 por defecto)
   * @param sort Ordenamiento ('id,desc' por defecto)
   * @param activa Filtro por estado activa (opcional: true, false o undefined para todas)
   */
  getAllSocias(page: number = 0, size: number = 10, sort: string = 'id,desc', activa?: boolean): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
   
    // Solo añadir el parámetro activa si está definido
    if (activa !== undefined) {
      params = params.set('activa', activa.toString());
    }
   
    return this.http.get(this.apiUrl, { params });
  }

  /**
   * Busca socias por texto con filtros opcionales
   * @param texto Texto a buscar
   * @param page Número de página (0 por defecto)
   * @param size Tamaño de página (10 por defecto)
   * @param activa Filtro por estado activa (opcional: true, false o undefined para todas)
   */
  searchSocias(texto: string, page: number = 0, size: number = 10, activa?: boolean): Observable<any> {
    let params = new HttpParams()
      .set('texto', texto)
      .set('page', page.toString())
      .set('size', size.toString());
   
    // Solo añadir el parámetro activa si está definido
    if (activa !== undefined) {
      params = params.set('activa', activa.toString());
    }
   
    return this.http.get(`${this.apiUrl}/buscar`, { params });
  }

  /**
   * Obtiene una socia por su ID
   */
  getSociaById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  /**
   * Crea una nueva socia
   */
  createSocia(socia: any): Observable<any> {
    return this.http.post(this.apiUrl, socia);
  }

  /**
   * Actualiza una socia existente
   */
  updateSocia(id: number, socia: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, socia);
  }

  /**
   * Elimina una socia
   */
  deleteSocia(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /**
   * Cambia el estado de una socia (activa/inactiva)
   */
  cambiarEstado(id: number, activa: boolean, observaciones?: string): Observable<any> {
    let params = new HttpParams()
      .set('activa', activa.toString());
   
    if (observaciones) {
      params = params.set('observaciones', observaciones);
    }
   
    return this.http.patch(`${this.apiUrl}/${id}/estado`, {}, { params });
  }

  // MÉTODOS AUXILIARES PARA FACILITAR EL USO

  /**
   * Obtiene solo socias activas
   */
  getSociasActivas(page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<any> {
    return this.getAllSocias(page, size, sort, true);
  }

  /**
   * Obtiene solo socias de baja
   */
  getSociasBaja(page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<any> {
    return this.getAllSocias(page, size, sort, false);
  }

  /**
   * Busca solo entre socias activas
   */
  searchSociasActivas(texto: string, page: number = 0, size: number = 10): Observable<any> {
    return this.searchSocias(texto, page, size, true);
  }

  /**
   * Busca solo entre socias de baja
   */
  searchSociasBaja(texto: string, page: number = 0, size: number = 10): Observable<any> {
    return this.searchSocias(texto, page, size, false);
  }

  /**
   * Obtiene el conteo total de socias activas
   */
  getCountSociasActivas(): Observable<any> {
    return this.getAllSocias(0, 1, 'id,desc', true);
  }

  /**
   * Obtiene el conteo total de socias de baja
   */
  getCountSociasBaja(): Observable<any> {
    return this.getAllSocias(0, 1, 'id,desc', false);
  }

  /**
   * Obtiene el conteo total de todas las socias
   */
  getCountAllSocias(): Observable<any> {
    return this.getAllSocias(0, 1, 'id,desc');
  }
}