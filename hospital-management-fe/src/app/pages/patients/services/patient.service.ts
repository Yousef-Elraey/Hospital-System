import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { PageResponse } from '../../../core/models/pagination.dto';
import type { PatientResponse } from '../models/response/patient-response.dto';
import type { CreatePatientRequest } from '../models/request/create-patient-request.dto';
import type { UpdatePatientRequest } from '../models/request/update-patient-request.dto';
import type { CreatePatientResponse } from '../models/response/create-patient-response.dto';
import type { UpdatePatientResponse } from '../models/response/update-patient-response.dto';
import type { PatientFilters } from '../models/request/patient-filters.dto';
import type { SearchPatientRequest } from '../models/request/search-patient-request.dto';
import type { MedicalRecordResponse } from '../../medical-records/models/response/medical-record-response.dto';

@Injectable({ providedIn: 'root' })
export class PatientService {
  constructor(private api: ApiHttpService) {}

  getPatients(filters?: PatientFilters): Observable<PageResponse<PatientResponse>> {
    return this.api.request<PageResponse<PatientResponse>>('POST', '/patient/search', filters ?? {});
  }

  searchPatient(body: SearchPatientRequest): Observable<PatientResponse> {
    return this.api.request<PageResponse<PatientResponse>>('POST', '/patient/search', { ...body, page: 0, size: 1 }).pipe(
      map((response) => {
        const patient = response.data?.[0];
        if (!patient) {
          throw new Error('no patient found');
        }
        return patient;
      }),
    );
  }

  getPatient(id: number): Observable<PatientResponse> {
    return this.api.request<PatientResponse>('GET', `/patient/${id}`);
  }

  addPatient(body: CreatePatientRequest): Observable<CreatePatientResponse> {
    return this.api.request<CreatePatientResponse>('POST', '/patient/patients', body);
  }

  updatePatient(id: number, body: UpdatePatientRequest): Observable<UpdatePatientResponse> {
    return this.api.request<UpdatePatientResponse>('PUT', `/patient/${id}`, body);
  }

  deletePatient(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/patient/${id}`);
  }

  getPatientHistory(id: number): Observable<MedicalRecordResponse[]> {
    return this.api.request<MedicalRecordResponse[]>('GET', `/patient/history/${id}`);
  }
}
