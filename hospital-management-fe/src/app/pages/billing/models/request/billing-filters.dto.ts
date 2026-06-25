import type { PageRequest } from '../../../../core/models/pagination.dto';

export type BillingFilters = PageRequest & {
  patient_id?: number;
  amount?: number;
};
