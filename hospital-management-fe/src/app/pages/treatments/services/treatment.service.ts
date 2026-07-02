import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { PageResponse } from '../../../core/models/pagination.dto';
import type { TreatmentResponse } from '../models/response/treatment-response.dto';
import type { CreateTreatmentRequest } from '../models/request/create-treatment-request.dto';
import type { UpdateTreatmentRequest } from '../models/request/update-treatment-request.dto';
import type { CreateTreatmentResponse } from '../models/response/create-treatment-response.dto';
import type { UpdateTreatmentResponse } from '../models/response/update-treatment-response.dto';
import type { PageRequest } from '../../../core/models/pagination.dto';

@Injectable({ providedIn: 'root' })
export class TreatmentService {
  constructor(private api: ApiHttpService) {}

  getTreatments(params?: PageRequest & { sortBy?: string; direction?: string }): Observable<PageResponse<TreatmentResponse>> {
    return this.api.request<PageResponse<TreatmentResponse>>('GET', '/treatment/treatments', undefined, {
      page: params?.page ?? 0,
      size: params?.size ?? 10,
      sortBy: params?.sortBy ?? 'id',
      direction: params?.direction ?? 'asc',
    });
  }

  getTreatment(id: number): Observable<TreatmentResponse> {
    return this.api.request<TreatmentResponse>('GET', `/treatment/${id}`);
  }

  addTreatment(body: CreateTreatmentRequest): Observable<CreateTreatmentResponse> {
    return this.api.request<CreateTreatmentResponse>('POST', '/treatment/treatments', body);
  }

  updateTreatment(body: UpdateTreatmentRequest): Observable<UpdateTreatmentResponse> {
    return this.api.request<UpdateTreatmentResponse>('PUT', '/treatment/update', body);
  }

  deleteTreatment(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/treatment/${id}`);
  }
}
