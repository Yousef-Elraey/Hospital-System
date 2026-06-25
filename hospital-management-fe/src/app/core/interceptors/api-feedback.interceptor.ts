import { HttpErrorResponse, HttpEvent, HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject, NgZone } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { catchError, tap, throwError } from 'rxjs';
import { MessageDialogComponent } from '../components/message-dialog/message-dialog.component';
import { SKIP_API_FEEDBACK } from '../tokens/api-feedback.tokens';
import { localizeApiErrorMessage } from '../utils/api-error-i18n';

const MUTATING = new Set(['POST', 'PUT', 'PATCH', 'DELETE']);

function isApiUrl(url: string): boolean {
  return url.includes('/api/') || url.endsWith('/api') || /\/api(\?|$)/.test(url);
}

function extractSuccessMessage(body: unknown, translate: TranslateService): string {
  if (body == null || body === '') {
    return translate.instant('apiFeedback.successDefault');
  }
  if (typeof body === 'string') {
    const t = body.trim();
    return t || translate.instant('apiFeedback.successDefault');
  }
  if (typeof body === 'object') {
    const o = body as Record<string, unknown>;
    const msg = o['message'];
    if (typeof msg === 'string' && msg.trim()) {
      return msg.trim();
    }
    if ('numberOfWaiting' in o && 'status' in o) {
      return translate.instant('apiFeedback.bookSuccess', {
        count: String(o['numberOfWaiting'] ?? ''),
        status: String(o['status'] ?? ''),
      });
    }
    return translate.instant('apiFeedback.successDefault');
  }
  return translate.instant('apiFeedback.successDefault');
}

function extractErrorMessage(err: HttpErrorResponse, translate: TranslateService): string {
  const body = err.error;
  if (typeof body === 'string' && body.trim()) {
    return body.trim();
  }
  if (body && typeof body === 'object') {
    const o = body as Record<string, unknown>;
    const msg = o['message'];
    if (typeof msg === 'string' && msg.trim()) {
      return msg.trim();
    }
    const errorsVal = o['errors'];
    if (Array.isArray(errorsVal) && errorsVal.length) {
      return errorsVal
        .map((e) =>
          typeof e === 'object' && e && 'defaultMessage' in e
            ? String((e as { defaultMessage: unknown })['defaultMessage'])
            : String(e)
        )
        .join('\n');
    }
    if (errorsVal && typeof errorsVal === 'object' && !Array.isArray(errorsVal)) {
      const fieldErrors = errorsVal as Record<string, string | string[]>;
      const parts = Object.entries(fieldErrors).flatMap(([k, v]) =>
        Array.isArray(v) ? v.map((m) => `${k}: ${m}`) : [`${k}: ${v}`]
      );
      if (parts.length) return parts.join('\n');
    }
    const errStr = o['error'];
    if (typeof errStr === 'string' && errStr.trim()) {
      return errStr.trim();
    }
  }
  if (err.status === 0) {
    return translate.instant('apiFeedback.errorNetwork');
  }
  if (err.message && !err.message.startsWith('Http failure response')) {
    return err.message;
  }
  return translate.instant('apiFeedback.errorGeneric', { status: String(err.status) });
}

function openFeedbackDialog(dialog: MatDialog, variant: 'success' | 'error', message: string): void {
  dialog.open(MessageDialogComponent, {
    data: { variant, message },
    width: 'min(420px, 92vw)',
    panelClass: ['api-feedback-dialog', variant === 'success' ? 'api-feedback-dialog--success' : 'api-feedback-dialog--error'],
    autoFocus: 'first-tabbable',
  });
}

export const apiFeedbackInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.context.get(SKIP_API_FEEDBACK) || !isApiUrl(req.url)) {
    return next(req);
  }

  const dialog = inject(MatDialog);
  const zone = inject(NgZone);
  const translate = inject(TranslateService);

  return next(req).pipe(
    tap((event: HttpEvent<unknown>) => {
      if (event instanceof HttpResponse) {
        const method = req.method.toUpperCase();
        if (MUTATING.has(method)) {
          const msg = extractSuccessMessage(event.body, translate);
          zone.run(() => openFeedbackDialog(dialog, 'success', msg));
        }
      }
    }),
    catchError((err: HttpErrorResponse) => {
      const raw = extractErrorMessage(err, translate);
      const msg = localizeApiErrorMessage(raw, translate);
      zone.run(() => openFeedbackDialog(dialog, 'error', msg));
      return throwError(() => err);
    })
  );
};
