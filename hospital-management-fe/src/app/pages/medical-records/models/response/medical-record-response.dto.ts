import type { AuditFields } from '../../../common/models/audit-fields.dto';

export interface MedicalRecordResponse extends AuditFields {
  id: number;
  diagnose: string;
  treatment: string;
  patientId: number;
  doctorId: number;
}
