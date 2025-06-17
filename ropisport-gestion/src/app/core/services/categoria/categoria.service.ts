import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Categoria } from '../../models/categoria';
import { CategoriaDTO } from '../../dtos/categoria-negocio-dto';
import { ApiResponse } from '../../models/api-response';
import { PaginatedResponse } from '../../models/paginated-response';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  private apiUrl = '/api/categorias';

  constructor(private http: HttpClient) {}

  // Método principal paginado
  getAllCategorias(page: number = 0, size: number = 10, sortBy: string = 'id', sortDirection: string = 'asc'): Observable<PaginatedResponse<Categoria>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PaginatedResponse<Categoria>>(this.apiUrl, { params });
  }

  // Método para obtener todas las categorías sin paginación (útil para dropdowns/selects)
  getAllCategoriasSimple(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.apiUrl}/all`);
  }

  // Búsqueda paginada por nombre
  searchCategorias(
    nombre: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'nombre',
    sortDirection: string = 'asc'
  ): Observable<PaginatedResponse<Categoria>> {
    const params = new HttpParams()
      .set('nombre', nombre)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PaginatedResponse<Categoria>>(`${this.apiUrl}/search`, { params });
  }

  // Búsqueda general paginada (similar a la de usuarios)
  busquedaGeneral(
    termino: string,
    activo?: boolean,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'nombre',
    sortDirection: string = 'asc'
  ): Observable<PaginatedResponse<Categoria>> {
    let params = new HttpParams()
      .set('termino', termino)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (activo !== undefined) {
      params = params.set('activo', activo.toString());
    }

    return this.http.get<PaginatedResponse<Categoria>>(`${this.apiUrl}/busqueda`, { params });
  }

  // Filtrar categorías activas/inactivas con paginación
  getCategoriasFiltradasPorEstado(
    activo: boolean,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'nombre',
    sortDirection: string = 'asc'
  ): Observable<PaginatedResponse<Categoria>> {
    const params = new HttpParams()
      .set('activo', activo.toString())
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PaginatedResponse<Categoria>>(`${this.apiUrl}/filtrar`, { params });
  }

  // Obtener categorías por rango de fechas con paginación
  getCategoriasPorFechas(
    fechaInicio: string,
    fechaFin: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'fechaCreacion',
    sortDirection: string = 'desc'
  ): Observable<PaginatedResponse<Categoria>> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PaginatedResponse<Categoria>>(`${this.apiUrl}/por-fechas`, { params });
  }

  // CRUD methods (sin cambios)
  getCategoriaById(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.apiUrl}/${id}`);
  }

  createCategoria(categoria: CategoriaDTO): Observable<Categoria> {
    return this.http.post<Categoria>(this.apiUrl, categoria);
  }

  updateCategoria(id: number, categoria: CategoriaDTO): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.apiUrl}/${id}`, categoria);
  }

  deleteCategoria(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.apiUrl}/${id}`);
  }

  // Método para activar/desactivar categoría
  toggleEstadoCategoria(id: number): Observable<Categoria> {
    return this.http.patch<Categoria>(`${this.apiUrl}/${id}/toggle-estado`, {});
  }

  // Método para obtener estadísticas de categorías
  getEstadisticasCategorias(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/estadisticas`);
  }

  // Método para validar si una categoría existe por nombre
  validarNombreCategoria(nombre: string, idExcluir?: number): Observable<boolean> {
    let params = new HttpParams().set('nombre', nombre);
    if (idExcluir) {
      params = params.set('idExcluir', idExcluir.toString());
    }
    return this.http.get<boolean>(`${this.apiUrl}/validar-nombre`, { params });
  }

  // Método para exportar categorías
  exportarCategorias(formato: 'excel' | 'pdf' | 'csv' = 'excel'): Observable<Blob> {
    const params = new HttpParams().set('formato', formato);
    return this.http.get(`${this.apiUrl}/exportar`, {
      params,
      responseType: 'blob'
    });
  }

  // Método para importar categorías
  importarCategorias(archivo: File): Observable<ApiResponse<any>> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/importar`, formData);
  }
}

