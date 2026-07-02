import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { PageRequest, PageResponse } from '../../../core/models/pagination.dto';
import type { DiagnoseResponse } from '../models/response/diagnose-response.dto';
import type { CreateDiagnoseRequest } from '../models/request/create-diagnose-request.dto';
import type { UpdateDiagnoseRequest } from '../models/request/update-diagnose-request.dto';
import type { CreateDiagnoseResponse } from '../models/response/create-diagnose-response.dto';
import type { UpdateDiagnoseResponse } from '../models/response/update-diagnose-response.dto';

@Injectable({ providedIn: 'root' })
export class DiagnoseService {
  constructor(private api: ApiHttpService) {}

  getDiagnoses(params?: PageRequest & { sortBy?: string; direction?: string }): Observable<PageResponse<DiagnoseResponse>> {
    return this.api.request<PageResponse<DiagnoseResponse>>('GET', '/diagnose/diagnoses', undefined, {
      page: params?.page ?? 0,
      size: params?.size ?? 10,
      sortBy: params?.sortBy ?? 'id',
      direction: params?.direction ?? 'asc',
    });
  }

  getDiagnose(id: number): Observable<DiagnoseResponse> {
    return this.api.request<DiagnoseResponse>('GET', `/diagnose/${id}`);
  }

  addDiagnose(body: CreateDiagnoseRequest): Observable<CreateDiagnoseResponse> {
    return this.api.request<CreateDiagnoseResponse>('POST', '/diagnose/diagnoses', body);
  }

  updateDiagnose(body: UpdateDiagnoseRequest): Observable<UpdateDiagnoseResponse> {
    return this.api.request<UpdateDiagnoseResponse>('PUT', '/diagnose/update', body);
  }

  deleteDiagnose(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/diagnose/${id}`);
  }
}
