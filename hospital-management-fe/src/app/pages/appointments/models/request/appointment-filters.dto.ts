import type { PageRequest } from '../../../../core/models/pagination.dto';

export type AppointmentFilters = PageRequest & {
  doctorId?: number;
  patientId?: number;
  status?: string;
  date?: string;
};
