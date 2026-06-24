import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { DoctorResponse } from '../models/response/doctor-response.dto';
import type { CreateDoctorRequest } from '../models/request/create-doctor-request.dto';
import type { UpdateDoctorRequest } from '../models/request/update-doctor-request.dto';
import type { CreateDoctorResponse } from '../models/response/create-doctor-response.dto';
import type { UpdateDoctorResponse } from '../models/response/update-doctor-response.dto';
import type { DoctorFilters } from '../models/request/doctor-filters.dto';

@Injectable({ providedIn: 'root' })
export class DoctorService {
  constructor(private api: ApiHttpService) {}

  getDoctors(filters?: DoctorFilters): Observable<DoctorResponse[]> {
    return this.api.request<DoctorResponse[]>('GET', '/doctor/doctors', undefined, filters);
  }

  getDoctor(id: number): Observable<DoctorResponse> {
    return this.api.request<DoctorResponse>('GET', `/doctor/${id}`);
  }

  addDoctor(body: CreateDoctorRequest): Observable<CreateDoctorResponse> {
    return this.api.request<CreateDoctorResponse>('POST', '/doctor/doctors', body);
  }

  updateDoctor(id: number, body: UpdateDoctorRequest): Observable<UpdateDoctorResponse> {
    return this.api.request<UpdateDoctorResponse>('PUT', `/doctor/${id}`, body);
  }

  deleteDoctor(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/doctor/${id}`);
  }
}
