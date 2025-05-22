import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UseStateService {

  private readonly USER_KEY = "ropisport";

  constructor() { }

  save(username: string, rol: string): void  {
    sessionStorage.setItem(this.USER_KEY, JSON.stringify({username, rol}));
  }

  getUsername(): string | null {
    const session = JSON.parse(<string>sessionStorage.getItem(this.USER_KEY));
    if (!session) {
      return null;
    }

    return session.username;
  }
  
  /*getUsername(): string | null {
    try {
      const session = JSON.parse(sessionStorage.getItem(this.USER_KEY) || '');
      return session?.username || null;
    } catch {
      return null;
    }
  }*/

  getRole(): string | null {
    const session = JSON.parse(<string>sessionStorage.getItem(this.USER_KEY));
    if (!session) {
      return null;
    }

    return session.rol;
  }

  removeSession(): void {
    sessionStorage.removeItem(this.USER_KEY);
  }

}
