import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { HttpLoadingService } from '../services/http-loading.service';
import { SKIP_HTTP_LOADING } from '../tokens/http-loading.tokens';

function isApiUrl(url: string): boolean {
  return url.includes('/api/') || url.endsWith('/api') || /\/api(\?|$)/.test(url);
}

export const httpLoadingInterceptor: HttpInterceptorFn = (req, next) => {
  if (!isApiUrl(req.url) || req.context.get(SKIP_HTTP_LOADING)) {
    return next(req);
  }

  const loading = inject(HttpLoadingService);
  loading.begin();
  return next(req).pipe(finalize(() => loading.end()));
};
