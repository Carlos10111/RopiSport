import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PaginatedResponse } from '../../models/paginated-response';
import { Empresa } from '../../models/empresa';
import { EmpresaDTO } from '../../dtos/empresa-dto';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private readonly API_URL = `${environment.apiUrl}/api/empresas`;

  constructor(private http: HttpClient) { }

  // Obtener todas las empresas con paginación
  getAllEmpresas(page: number = 0, size: number = 10, sortBy: string = 'id'): Observable<PaginatedResponse<Empresa>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy);

    return this.http.get<PaginatedResponse<Empresa>>(this.API_URL, { params });
  }

  // Obtener empresa por ID
  getEmpresaById(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.API_URL}/${id}`);
  }

  // Obtener empresa por ID de socia
  getEmpresaBySociaId(sociaId: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.API_URL}/socia/${sociaId}`);
  }

  // Crear nueva empresa
  createEmpresa(empresaDTO: EmpresaDTO): Observable<Empresa> {
    return this.http.post<Empresa>(this.API_URL, empresaDTO);
  }

  // Actualizar empresa
  updateEmpresa(id: number, empresaDTO: EmpresaDTO): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.API_URL}/${id}`, empresaDTO);
  }

  // Eliminar empresa
  deleteEmpresa(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

  // Búsqueda simple (para compatibilidad con el componente de pagos)
  searchEmpresas(searchTerm: string, page: number = 0, size: number = 10): Observable<PaginatedResponse<Empresa>> {
    const params = new HttpParams()
      .set('texto', searchTerm)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PaginatedResponse<Empresa>>(`${this.API_URL}/buscar`, { params });
  }

  // Búsqueda general
  busquedaGeneral(
    texto?: string,
    activa?: boolean,
    page: number = 0,
    size: number = 10,
    sort: string = 'id,desc'
  ): Observable<PaginatedResponse<Empresa>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (texto) {
      params = params.set('texto', texto);
    }
    if (activa !== undefined) {
      params = params.set('activa', activa.toString());
    }

    return this.http.get<PaginatedResponse<Empresa>>(`${this.API_URL}/buscar`, { params });
  }

  // Búsqueda avanzada
  busquedaAvanzada(
    nombreNegocio?: string,
    cif?: string,
    email?: string,
    telefono?: string,
    direccion?: string,
    categoriaId?: number,
    activa?: boolean,
    page: number = 0,
    size: number = 10,
    sort: string = 'id,desc'
  ): Observable<PaginatedResponse<Empresa>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (nombreNegocio) params = params.set('nombreNegocio', nombreNegocio);
    if (cif) params = params.set('cif', cif);
    if (email) params = params.set('email', email);
    if (telefono) params = params.set('telefono', telefono);
    if (direccion) params = params.set('direccion', direccion);
    if (categoriaId) params = params.set('categoriaId', categoriaId.toString());
    if (activa !== undefined) params = params.set('activa', activa.toString());

    return this.http.get<PaginatedResponse<Empresa>>(`${this.API_URL}/busqueda-avanzada`, { params });
  }

  // Exportar a Excel
  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.API_URL}/excel`, { responseType: 'blob' });
  }
}