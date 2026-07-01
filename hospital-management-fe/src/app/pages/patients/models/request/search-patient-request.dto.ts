import type { PageRequest } from '../../../../core/models/pagination.dto';

export type SearchPatientRequest = PageRequest & {
  phone?: string;
  name?: string;
  dateOfBirth?: string;
  mobile?: string;
};
