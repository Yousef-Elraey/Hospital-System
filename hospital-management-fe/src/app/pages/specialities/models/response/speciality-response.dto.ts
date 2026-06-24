import type { AuditFields } from '../../../common/models/audit-fields.dto';

export interface SpecialityResponse extends AuditFields {
  id: number;
  nameEn: string;
  nameAr: string;
}
