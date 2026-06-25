export interface UpdateAppointmentRequest {
  timing: string;
  doctorId: number;
  patientId: number;
  status?: string;
}
