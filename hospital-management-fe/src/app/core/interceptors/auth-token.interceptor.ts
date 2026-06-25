import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';

function isApiUrl(url: string): boolean {
  return url.includes('/api/') || url.endsWith('/api') || /\/api(\?|$)/.test(url);
}

export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {
  if (!isApiUrl(req.url)) return next(req);

  const auth = inject(AuthService);
  const token = auth.token;
  if (!token) return next(req);

  const withAuth = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });
  return next(withAuth);
};
