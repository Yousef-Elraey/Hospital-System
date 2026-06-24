import { HttpContextToken } from '@angular/common/http';

/** Set on a request to skip the global loading overlay. */
export const SKIP_HTTP_LOADING = new HttpContextToken<boolean>(() => false);
