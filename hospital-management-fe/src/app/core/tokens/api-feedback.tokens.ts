import { HttpContextToken } from '@angular/common/http';

/** Set on a request to skip success/error feedback dialogs (rare). */
export const SKIP_API_FEEDBACK = new HttpContextToken<boolean>(() => false);
