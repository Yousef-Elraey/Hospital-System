import type { AuditFields } from '../../../common/models/audit-fields.dto';

export interface BillingResponse extends AuditFields {
  id: number;
  amount: number;
  patient_id: number;
}
