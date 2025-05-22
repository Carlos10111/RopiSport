import { HttpInterceptorFn } from '@angular/common/http';
import {TokenService} from '../auth/token.service';
import {inject} from '@angular/core';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/auth/login') /*|| req.url.includes('/auth/register')*/) {
    return next(req.clone({
      setHeaders: {
        'Content-Type': 'application/json'
      }
    }));
  }
  /*
  header = {
  'Content-Type': 'application/json',
  }
  {
  ...header,
  'Authentication': 'Bearer ' + accessToken -> if accessToken
  }
   */

  const tokenService = inject(TokenService);
  const accessToken = tokenService.getAccessToken();

  const cloneReq = req.clone({
    setHeaders: {
      'Content-Type': 'application/json',
      ...(accessToken ? {/*'Authentication'*/Authorization: 'Bearer ' + accessToken} : undefined)
    }
  });

  return next(cloneReq);

};
