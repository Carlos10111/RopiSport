import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UseStateService {
  private readonly USER_STATE_KEY = 'user_state';

  save(username: string, rol: string): void {
    const userData = { username, rol };
    localStorage.setItem(this.USER_STATE_KEY, JSON.stringify(userData));
  }

  getCurrentUser(): any {
    const userData = localStorage.getItem(this.USER_STATE_KEY);
    return userData ? JSON.parse(userData) : null;
  }

  clear(): void {
    localStorage.removeItem(this.USER_STATE_KEY);
  }

  // Agregar este método que falta
  removeSession(): void {
    this.clear(); // Hace lo mismo que clear()
  }

  // Métodos adicionales útiles
  getUsername(): string | null {
    const user = this.getCurrentUser();
    return user?.username || null;
  }

  getUserRole(): string | null {
    const user = this.getCurrentUser();
    return user?.rol || null;
  }

  isUserLoggedIn(): boolean {
    return this.getCurrentUser() !== null;
  }
}