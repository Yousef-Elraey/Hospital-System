export interface UpdateMedicalRecordRequest {
  diagnose: string;
  treatment: string;
  patientId: number;
  doctorId: number;
}
