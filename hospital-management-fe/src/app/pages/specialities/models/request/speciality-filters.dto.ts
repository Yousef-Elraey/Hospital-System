import type { PageRequest } from '../../../../core/models/pagination.dto';

export type SpecialityFilters = PageRequest & {
  nameEn?: string;
  nameAr?: string;
};
