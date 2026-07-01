import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { PageResponse } from '../../../core/models/pagination.dto';
import type { SpecialityResponse } from '../models/response/speciality-response.dto';
import type { CreateSpecialityRequest } from '../models/request/create-speciality-request.dto';
import type { UpdateSpecialityRequest } from '../models/request/update-speciality-request.dto';
import type { CreateSpecialityResponse } from '../models/response/create-speciality-response.dto';
import type { UpdateSpecialityResponse } from '../models/response/update-speciality-response.dto';
import type { SpecialityFilters } from '../models/request/speciality-filters.dto';

@Injectable({ providedIn: 'root' })
export class SpecialityService {
  constructor(private api: ApiHttpService) {}

  getSpecialities(filters?: SpecialityFilters): Observable<PageResponse<SpecialityResponse>> {
    return this.api.request<PageResponse<SpecialityResponse>>('POST', '/speciality/search', filters ?? {});
  }

  getSpeciality(id: number): Observable<SpecialityResponse> {
    return this.api.request<SpecialityResponse>('GET', `/speciality/${id}`);
  }

  addSpeciality(body: CreateSpecialityRequest): Observable<CreateSpecialityResponse> {
    return this.api.request<CreateSpecialityResponse>('POST', '/speciality/specialities', body);
  }

  updateSpeciality(id: number, body: UpdateSpecialityRequest): Observable<UpdateSpecialityResponse> {
    return this.api.request<UpdateSpecialityResponse>('PUT', `/speciality/${id}`, body);
  }

  deleteSpeciality(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/speciality/${id}`);
  }
}
