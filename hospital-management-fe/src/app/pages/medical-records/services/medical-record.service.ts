import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { MedicalRecordResponse } from '../models/response/medical-record-response.dto';
import type { CreateMedicalRecordRequest } from '../models/request/create-medical-record-request.dto';
import type { UpdateMedicalRecordRequest } from '../models/request/update-medical-record-request.dto';
import type { CreateMedicalRecordResponse } from '../models/response/create-medical-record-response.dto';
import type { UpdateMedicalRecordResponse } from '../models/response/update-medical-record-response.dto';
import type { MedicalRecordFilters } from '../models/request/medical-record-filters.dto';

@Injectable({ providedIn: 'root' })
export class MedicalRecordService {
  constructor(private api: ApiHttpService) {}

  getMedicalRecords(filters?: MedicalRecordFilters): Observable<MedicalRecordResponse[]> {
    return this.api.request<MedicalRecordResponse[]>('GET', '/medical-record/medical-records', undefined, filters);
  }

  getMedicalRecord(id: number): Observable<MedicalRecordResponse> {
    return this.api.request<MedicalRecordResponse>('GET', `/medical-record/${id}`);
  }

  addMedicalRecord(body: CreateMedicalRecordRequest): Observable<CreateMedicalRecordResponse> {
    return this.api.request<CreateMedicalRecordResponse>('POST', '/medical-record/medical-records', body);
  }

  updateMedicalRecord(id: number, body: UpdateMedicalRecordRequest): Observable<UpdateMedicalRecordResponse> {
    return this.api.request<UpdateMedicalRecordResponse>('PUT', `/medical-record/${id}`, body);
  }

  deleteMedicalRecord(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/medical-record/${id}`);
  }

  getMedicalRecordsByPatientId(id: number): Observable<MedicalRecordResponse[]> {
    return this.api.request<MedicalRecordResponse[]>('GET', `/medical-record/medical-record-patient-id/${id}`);
  }
}
