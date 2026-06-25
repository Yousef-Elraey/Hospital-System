import type { PageRequest } from '../../../../core/models/pagination.dto';

export type DoctorFilters = PageRequest & {
  name?: string;
  speciality?: string;
  contactNumber?: string;
};
