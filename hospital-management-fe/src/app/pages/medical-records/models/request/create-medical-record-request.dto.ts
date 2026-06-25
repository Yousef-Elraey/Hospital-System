export interface CreateMedicalRecordRequest {
  diagnose: string;
  treatment: string;
  patientId: number;
  doctorId: number;
}
