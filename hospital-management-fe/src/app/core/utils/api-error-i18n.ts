import { TranslateService } from '@ngx-translate/core';

/** Map backend messages (case-insensitive) to i18n keys under apiErrors.detail.* */
const DETAIL_KEY_BY_PATTERN: { test: (s: string) => boolean; key: string }[] = [
  {
    test: (s) => /phone number is already exist/i.test(s),
    key: 'apiErrors.detail.phoneAlreadyExists',
  },
  {
    test: (s) => /no billings found/i.test(s),
    key: 'apiErrors.detail.noBillingsFound',
  },
  {
    test: (s) => /no appointments found/i.test(s),
    key: 'apiErrors.detail.noAppointmentsFound',
  },
  {
    test: (s) => /no medical[_\s-]*records found/i.test(s),
    key: 'apiErrors.detail.noMedicalRecordsFound',
  },
];

/**
 * Turns backend English messages into localized strings using ngx-translate when a mapping exists.
 */
export function localizeApiErrorMessage(raw: string, translate: TranslateService): string {
  const text = raw.trim();
  if (!text) return text;

  if (text.includes('\n')) {
    return text
      .split('\n')
      .map((line) => localizeApiErrorMessage(line, translate))
      .join('\n');
  }

  for (const { test, key } of DETAIL_KEY_BY_PATTERN) {
    if (test(text)) {
      return translate.instant(key);
    }
  }

  return text;
}
