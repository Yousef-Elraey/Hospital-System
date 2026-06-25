import type { PageRequest } from '../../../../core/models/pagination.dto';

export type MedicalRecordFilters = PageRequest & {
  patientId?: number;
  doctorId?: number;
  diagnose?: string;
};
