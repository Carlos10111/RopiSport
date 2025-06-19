import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { TipoInstitucion } from '../../models/tipo-institucion';
import { TipoInstitucionDTO } from '../../dtos/tipo-institucion-dto';
import { ApiResponse } from '../../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class TipoInstitucionService {
  private apiUrl = `${environment.apiUrl}/tipos-institucion`;

  constructor(private http: HttpClient) {}

  // Get all TipoInstitucion
  getAllTiposInstitucion(): Observable<TipoInstitucion[]> {
    return this.http.get<TipoInstitucion[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  // Get TipoInstitucion by ID
  getTipoInstitucionById(id: number): Observable<TipoInstitucion> {
    return this.http.get<TipoInstitucion>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Create a new TipoInstitucion
  createTipoInstitucion(tipo: TipoInstitucionDTO): Observable<TipoInstitucion> {
    return this.http.post<TipoInstitucion>(this.apiUrl, tipo).pipe(
      catchError(this.handleError)
    );
  }

  // Update an existing TipoInstitucion
  updateTipoInstitucion(id: number, tipo: TipoInstitucionDTO): Observable<TipoInstitucion> {
    return this.http.put<TipoInstitucion>(`${this.apiUrl}/${id}`, tipo).pipe(
      catchError(this.handleError)
    );
  }

  // Delete a TipoInstitucion
  deleteTipoInstitucion(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Search TipoInstitucion by name
  searchTiposByNombre(nombre: string): Observable<TipoInstitucion[]> {
    return this.http.get<TipoInstitucion[]>(`${this.apiUrl}/search?nombre=${encodeURIComponent(nombre)}`).pipe(
      catchError(this.handleError)
    );
  }

  // Error handling
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}