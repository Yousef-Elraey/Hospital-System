import type { AppointmentResponse } from '../models/response/appointment-response.dto';
import { isTodayFromIso } from '../../../core/utils/is-today';

export interface AppointmentTodayStats {
  booked: number;
  paid: number;
  unpaid: number;
  waiting: number;
  completed: number;
}

const EMPTY_STATS: AppointmentTodayStats = {
  booked: 0,
  paid: 0,
  unpaid: 0,
  waiting: 0,
  completed: 0,
};

function normalizeStatus(status?: string): string {
  return (status ?? '').toUpperCase().trim();
}

function classifyTodayAppointment(status?: string): keyof AppointmentTodayStats {
  const s = normalizeStatus(status);
  if (['COMPLETED', 'FINISHED', 'DONE', 'ENDED'].includes(s)) return 'completed';
  if (s === 'PAID') return 'paid';
  if (['UNPAID', 'NOT_PAID', 'NOTPAID'].includes(s)) return 'unpaid';
  if (['PENDING', 'WAITING'].includes(s)) return 'waiting';
  if (['BOOKED', 'RESERVED'].includes(s)) return 'booked';
  return 'booked';
}

export function computeAppointmentTodayStats(appointments: AppointmentResponse[]): AppointmentTodayStats {
  const stats = { ...EMPTY_STATS };
  for (const appointment of appointments) {
    if (!isTodayFromIso(appointment.timing)) continue;
    stats[classifyTodayAppointment(appointment.status)]++;
  }
  return stats;
}
