import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenService } from '../auth/token.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  
  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {
    console.log('🚀 TokenInterceptor creado');
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('🔄 INTERCEPTOR EJECUTÁNDOSE');
    console.log('📍 URL:', req.url);
    console.log('🔧 Método:', req.method);

    // No procesar peticiones de autenticación
    if (this.isAuthRequest(req.url)) {
      console.log('🚫 Es petición de auth, saltando interceptor');
      return next.handle(req);
    }

    // Obtener token
    const token = this.tokenService.getToken();
    console.log('🔑 Token para interceptor:', token ? `${token.substring(0, 20)}...` : 'NO HAY TOKEN');
    
    if (token && !this.tokenService.isTokenExpired(token)) {
      // Clonar petición y añadir Authorization header
      const authReq = req.clone({ 
        setHeaders: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      
      console.log('✅ Token añadido a la petición');
      console.log('📋 Headers finales:', authReq.headers.keys());
      console.log('🔐 Authorization header:', authReq.headers.get('Authorization')?.substring(0, 50) + '...');
      
      return next.handle(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('❌ Error en petición autenticada:', error.status, error.message);
          return this.handleAuthError(error);
        })
      );
    }

    console.log('❌ Sin token válido - enviando petición sin autenticación');
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('❌ Error en petición sin token:', error.status, error.message);
        return this.handleAuthError(error);
      })
    );
  }

  private isAuthRequest(url: string): boolean {
    const authUrls = ['/auth/login', '/auth/register', '/auth/refresh'];
    const isAuth = authUrls.some(authUrl => url.includes(authUrl));
    console.log('🔍 ¿Es petición de auth?', isAuth, 'para URL:', url);
    return isAuth;
  }

  private handleAuthError(error: HttpErrorResponse): Observable<never> {
    if (error.status === 401 || error.status === 403) {
      console.log('🚨 Error de autenticación detectado - limpiando y redirigiendo');
      this.tokenService.clearAll();
      this.router.navigate(['/login']);
    }
    return throwError(() => error);
  }
  
}