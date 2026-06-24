import type { AppointmentSlotResponse } from './appointment-slot-response.dto';

export interface GenerateSlotsResponse {
  createdCount: number;
  slots: AppointmentSlotResponse[];
}
