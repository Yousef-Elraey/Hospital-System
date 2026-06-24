import type { AuditFields } from '../../../common/models/audit-fields.dto';

export interface AppointmentResponse extends AuditFields {
  id: number;
  timing: string;
  doctorId: number;
  patientId: number;
  status?: string;
}
