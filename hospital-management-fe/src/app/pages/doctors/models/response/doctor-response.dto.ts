import type { AuditFields } from '../../../common/models/audit-fields.dto';

export interface DoctorSpeciality {
  id: number;
  nameEn?: string;
  nameAr?: string;
  name_en?: string;
  name_ar?: string;
}

export interface DoctorResponse extends AuditFields {
  id: number;
  name: string;
  speciality?: DoctorSpeciality;
  contactNumber: string;
}
