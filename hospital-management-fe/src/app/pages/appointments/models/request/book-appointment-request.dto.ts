import type { AppointmentType } from './appointment-type.dto';

export interface BookAppointmentRequest {
  patientId?: number;
  patientName?: string;
  patientGender?: string;
  patientPhone?: string;
  patientDateOfBirth?: string;
  appointmentTiming: string;
  appointmentType?: AppointmentType;
  doctorId: number;
  statusId?: string;
}
