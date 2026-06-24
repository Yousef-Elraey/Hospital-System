import type { AuditFields } from '../../../common/models/audit-fields.dto';

export interface DoctorResponse extends AuditFields {
  id: number;
  name: string;
  speciality: string;
  contactNumber: string;
}
