import type { AuditFields } from '../../../common/models/audit-fields.dto';
import type { MedicalRecordResponse } from '../../../medical-records/models/response/medical-record-response.dto';

export interface PatientResponse extends AuditFields {
  id: number;
  name: string;
  gender: string;
  phone: string;
  dateOfBirth?: string;
  medicalRecords?: MedicalRecordResponse[];
}
