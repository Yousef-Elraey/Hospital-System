import type { Lang } from '../services/locale.service';

const ARABIC_INDIC = '٠١٢٣٤٥٦٧٨٩';

export function toLatinDigits(str: string): string {
  return String(str ?? '').replace(/[٠-٩]/g, (ch) => String('٠١٢٣٤٥٦٧٨٩'.indexOf(ch)));
}

/** Eastern Arabic numerals (٠–٩); used for `ar` display in forms and datepicker. */
export function toArabicIndicDigits(str: string): string {
  return String(str ?? '').replace(/\d/g, (d) => ARABIC_INDIC[+d] ?? d);
}

function pad2(n: number): string {
  return String(n).padStart(2, '0');
}

function isValidYmd(y: number, m: number, d: number): boolean {
  if (!Number.isInteger(y) || m < 1 || m > 12 || d < 1 || d > 31) return false;
  const dt = new Date(y, m - 1, d);
  return dt.getFullYear() === y && dt.getMonth() === m - 1 && dt.getDate() === d;
}

/**
 * API / model date (YYYY-MM-DD) → DD-MM-YYYY for both locales (day on the right in RTL reads naturally).
 * Arabic uses eastern numerals.
 */
export function formatDateDisplay(value: string | undefined | null, lang: Lang): string {
  if (value == null || String(value).trim() === '') return '-';
  const raw = String(value).trim();
  let y: number;
  let m: number;
  let d: number;
  const isoHead = raw.slice(0, 10);
  if (/^\d{4}-\d{2}-\d{2}$/.test(isoHead)) {
    [y, m, d] = isoHead.split('-').map(Number);
  } else {
    const probe = new Date(raw.length === 10 ? `${raw}T12:00:00` : raw);
    if (isNaN(probe.getTime())) return raw;
    y = probe.getFullYear();
    m = probe.getMonth() + 1;
    d = probe.getDate();
  }
  if (!isValidYmd(y, m, d)) return raw;
  const latin = `${pad2(d)}-${pad2(m)}-${y}`;
  return lang === 'ar' ? toArabicIndicDigits(latin) : latin;
}

/**
 * Parses a user-visible date into YYYY-MM-DD.
 * Locale display is DD-MM-YYYY (en Latin, ar eastern digits); still accepts ISO YYYY-MM-DD.
 * Also accepts legacy YYYY-MM-DD typed in Arabic for robustness.
 */
export function parseDisplayDateToIso(raw: string | number | undefined | null, lang: Lang): string | null {
  if (raw == null) return null;
  const v = toLatinDigits(String(raw).trim());
  if (!v) return null;
  if (/^\d{4}-\d{2}-\d{2}$/.test(v.slice(0, 10))) {
    const iso = v.slice(0, 10);
    const [y, m, d] = iso.split('-').map(Number);
    return isValidYmd(y, m, d) ? iso : null;
  }
  const dmY = v.match(/^(\d{1,2})-(\d{1,2})-(\d{4})$/);
  if (dmY) {
    const d0 = +dmY[1];
    const mo = +dmY[2];
    const y = +dmY[3];
    if (!isValidYmd(y, mo, d0)) return null;
    return `${y}-${pad2(mo)}-${pad2(d0)}`;
  }
  if (lang === 'ar') {
    const ymd = v.match(/^(\d{4})-(\d{1,2})-(\d{1,2})$/);
    if (ymd) {
      const y = +ymd[1];
      const mo = +ymd[2];
      const d0 = +ymd[3];
      if (!isValidYmd(y, mo, d0)) return null;
      return `${y}-${pad2(mo)}-${pad2(d0)}`;
    }
  }
  return null;
}
