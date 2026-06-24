import { format, isValid, parseISO } from 'date-fns';

function isoDateStringToDate(s: string | undefined | null): Date | null {
  if (!s || s.length < 10) return null;
  const d = parseISO(s.slice(0, 10));
  return isValid(d) ? d : null;
}

function combineAppointmentDateAndTime(date: Date | null, time: string): string {
  if (!date || !isValid(date)) return '';
  if (!time || !/^\d{1,2}:\d{2}$/.test(time)) return '';
  const [h, m] = time.split(':').map((x) => +x);
  const out = new Date(date.getFullYear(), date.getMonth(), date.getDate(), h, m, 0, 0);
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${out.getFullYear()}-${pad(out.getMonth() + 1)}-${pad(out.getDate())}T${pad(out.getHours())}:${pad(out.getMinutes())}`;
}

export function combineSlotIsoDateAndTime(dateIso: string, time: string): string {
  return combineAppointmentDateAndTime(isoDateStringToDate(dateIso), time);
}
