export interface CreateAppointmentRequest {
  timing: string;
  doctorId: number;
  patientId: number;
  status?: string;
}
