import { format, isValid, parseISO } from 'date-fns';
import type { Lang } from '../../../core/services/locale.service';
import { formatDateDisplay } from '../../../core/utils/display-date';

export function isoDateStringToDate(s: string | undefined | null): Date | null {
  if (!s || s.length < 10) return null;
  const d = parseISO(s.slice(0, 10));
  return isValid(d) ? d : null;
}

export function dateToIsoDateString(d: Date | null): string {
  if (!d || !isValid(d)) return '';
  return format(d, 'yyyy-MM-dd');
}

export function parseAppointmentTiming(iso: string | undefined | null): { date: Date | null; time: string } {
  if (!iso) return { date: null, time: '' };
  const d = parseISO(iso);
  if (!isValid(d)) return { date: null, time: '' };
  const pad = (n: number) => String(n).padStart(2, '0');
  return {
    date: new Date(d.getFullYear(), d.getMonth(), d.getDate()),
    time: `${pad(d.getHours())}:${pad(d.getMinutes())}`,
  };
}

/** Same shape as native `datetime-local` value (local wall time, no timezone suffix). */
export function combineAppointmentDateAndTime(date: Date | null, time: string): string {
  if (!date || !isValid(date)) return '';
  if (!time || !/^\d{1,2}:\d{2}$/.test(time)) return '';
  const [h, m] = time.split(':').map((x) => +x);
  const out = new Date(date.getFullYear(), date.getMonth(), date.getDate(), h, m, 0, 0);
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${out.getFullYear()}-${pad(out.getMonth() + 1)}-${pad(out.getDate())}T${pad(out.getHours())}:${pad(out.getMinutes())}`;
}

/** Same as `combineAppointmentDateAndTime` but accepts `yyyy-MM-dd` from the hospital datepicker. */
export function combineAppointmentIsoDateAndTime(dateIso: string, time: string): string {
  return combineAppointmentDateAndTime(isoDateStringToDate(dateIso), time);
}

/** Appointment `timing` (ISO-like local datetime) → date part per locale + `HH:mm`. */
export function formatDateTimeDisplay(iso: string | undefined | null, lang: Lang): string {
  const s = iso == null ? '' : String(iso).trim();
  if (!s) return '-';
  const { date, time } = parseAppointmentTiming(s);
  if (!date) return s;
  const datePart = formatDateDisplay(dateToIsoDateString(date), lang);
  return time ? `${datePart} ${time}` : datePart;
}
