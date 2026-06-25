import type { AppointmentSlotStatus } from './appointment-slot-status.dto';

export interface AppointmentSlotFilters {
  doctorId?: number;
  startDate?: string;
  endDate?: string;
  status?: AppointmentSlotStatus;
}
