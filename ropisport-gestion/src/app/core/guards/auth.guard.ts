import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { TokenService } from '../auth/token.service';

export const AuthGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  // Si no está autenticado, redirigir al login
  if (!tokenService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};