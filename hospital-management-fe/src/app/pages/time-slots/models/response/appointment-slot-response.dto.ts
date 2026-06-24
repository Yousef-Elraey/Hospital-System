import type { AuditFields } from '../../../common/models/audit-fields.dto';
import type { AppointmentSlotStatus } from '../request/appointment-slot-status.dto';

export interface AppointmentSlotResponse extends AuditFields {
  id: number;
  doctorId: number;
  startTime: string;
  endTime: string;
  status: AppointmentSlotStatus;
}
