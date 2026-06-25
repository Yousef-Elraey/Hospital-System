import type { Lang } from '../../../core/services/locale.service';
import { formatDateDisplay } from '../../../core/utils/display-date';

/** Patient DOB and other API date-only fields (YYYY-MM-DD) for list/view/add/edit. */
export function formatDateOfBirth(value: string | undefined | null, lang: Lang = 'en'): string {
  return formatDateDisplay(value, lang);
}
