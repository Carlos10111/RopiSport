import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { TokenService } from '../auth/token.service';

export const PublicGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  // Si el usuario ya está autenticado, redirigir al panel de admin
  if (tokenService.isAuthenticated()) {
    router.navigate(['/admin']);
    return false;
  }

  return true;
};