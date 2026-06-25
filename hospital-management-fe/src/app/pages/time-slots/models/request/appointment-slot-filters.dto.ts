import type { PageRequest } from '../../../../core/models/pagination.dto';
import type { AppointmentSlotStatus } from './appointment-slot-status.dto';

export type AppointmentSlotFilters = PageRequest & {
  doctorId?: number;
  startDate?: string;
  endDate?: string;
  status?: AppointmentSlotStatus;
};
