import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';

  setToken(token: string): void {
    console.log('💾 Guardando token:', token?.substring(0, 20) + '...');
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    console.log('🔍 Token obtenido desde localStorage:', token ? token.substring(0, 20) + '...' : 'NO HAY TOKEN');
    return token;
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token !== null && !this.isTokenExpired(token);
  }

  isTokenExpired(token: string): boolean {
    if (!token) return true;
    
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp * 1000;
      const isExpired = Date.now() >= exp;
      console.log('🕒 Token expirado:', isExpired);
      return isExpired;
    } catch (error) {
      console.error('❌ Error verificando token:', error);
      return true;
    }
  }

  clearAll(): void {
    console.log('🗑️ Limpiando tokens');
    localStorage.removeItem(this.TOKEN_KEY);
  }
} 