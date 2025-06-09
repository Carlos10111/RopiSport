import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Usuario } from '../../models/usuario';
import { UsuarioDTO } from '../../dtos/usuario-dto';
import { ApiResponse } from '../../models/api-response';
import { Rol } from '../../models/rol';
import { PaginatedResponse } from '../../models/paginated-response';
import { PasswordChangeDTO } from '../../dtos/password-change-dto';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) { }

  // Obtener todos los usuarios con paginación
  getAllUsuarios(page: number = 0, size: number = 10, sortBy: string = 'id'): Observable<PaginatedResponse<Usuario>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy);

    return this.http.get<PaginatedResponse<Usuario>>(`${environment.apiUrl}/usuarios`, { params });
  }

  // Obtener usuario por ID
  getUsuarioById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${environment.apiUrl}/usuarios/${id}`);
  }

  // Obtener usuario por username
  getUsuarioByUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${environment.apiUrl}/usuarios/username/${username}`);
  }

  // Obtener usuarios por rol
  getUsuariosByRol(rolId: number): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${environment.apiUrl}/usuarios/rol/${rolId}`);
  }

  // Búsqueda general
  busquedaGeneral(
    texto?: string, 
    activo?: boolean, 
    rolId?: number, 
    page: number = 0, 
    size: number = 10, 
    sort: string = 'id,desc'
  ): Observable<PaginatedResponse<Usuario>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (texto) {
      params = params.set('texto', texto);
    }
    if (activo !== undefined) {
      params = params.set('activo', activo.toString());
    }
    if (rolId) {
      params = params.set('rolId', rolId.toString());
    }

    return this.http.get<PaginatedResponse<Usuario>>(`${environment.apiUrl}/usuarios/buscar`, { params });
  }

  // Búsqueda avanzada
  busquedaAvanzada(
    username?: string,
    email?: string,
    nombreCompleto?: string,
    rolId?: number,
    activo?: boolean,
    page: number = 0,
    size: number = 10,
    sort: string = 'id,desc'
  ): Observable<PaginatedResponse<Usuario>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (username) {
      params = params.set('username', username);
    }
    if (email) {
      params = params.set('email', email);
    }
    if (nombreCompleto) {
      params = params.set('nombreCompleto', nombreCompleto);
    }
    if (rolId) {
      params = params.set('rolId', rolId.toString());
    }
    if (activo !== undefined) {
      params = params.set('activo', activo.toString());
    }

    return this.http.get<PaginatedResponse<Usuario>>(`${environment.apiUrl}/usuarios/busqueda-avanzada`, { params });
  }

  // Crear usuario general
  createUsuario(usuario: UsuarioDTO): Observable<Usuario> {
    return this.http.post<Usuario>(`${environment.apiUrl}/usuarios`, usuario);
  }

  // Crear administrador general
  crearAdministradorGeneral(usuario: UsuarioDTO): Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(`${environment.apiUrl}/usuarios/admin-general`, usuario);
  }

  // Crear administrador de socias
  crearAdministradorSocias(usuario: UsuarioDTO): Observable<ApiResponse<Usuario>> {
    return this.http.post<ApiResponse<Usuario>>(`${environment.apiUrl}/usuarios/admin-socias`, usuario);
  }

  // Actualizar usuario
  updateUsuario(id: number, usuario: UsuarioDTO): Observable<Usuario> {
    return this.http.put<Usuario>(`${environment.apiUrl}/usuarios/${id}`, usuario);
  }

  // Eliminar usuario
  deleteUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/usuarios/${id}`);
  }

  // Cambiar contraseña
  changePassword(id: number, request: PasswordChangeDTO): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${environment.apiUrl}/usuarios/${id}/change-password`, request);
  }

  // Obtener roles (asumiendo que tienes un endpoint para esto)
  getRoles(): Observable<Rol[]> {
    return this.http.get<Rol[]>(`${environment.apiUrl}/roles`);
  }
}
