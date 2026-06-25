import type { PageRequest } from '../../../../core/models/pagination.dto';

export type PatientFilters = PageRequest & {
  name?: string;
  dateOfBirth?: string;
  phone?: string;
  mobile?: string;
};
