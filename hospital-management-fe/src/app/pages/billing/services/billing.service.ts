import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { PageResponse } from '../../../core/models/pagination.dto';
import type { BillingResponse } from '../models/response/billing-response.dto';
import type { CreateBillingRequest } from '../models/request/create-billing-request.dto';
import type { UpdateBillingRequest } from '../models/request/update-billing-request.dto';
import type { CreateBillingResponse } from '../models/response/create-billing-response.dto';
import type { UpdateBillingResponse } from '../models/response/update-billing-response.dto';
import type { BillingFilters } from '../models/request/billing-filters.dto';

@Injectable({ providedIn: 'root' })
export class BillingService {
  constructor(private api: ApiHttpService) {}

  getBillings(filters?: BillingFilters): Observable<PageResponse<BillingResponse>> {
    return this.api.request<PageResponse<BillingResponse>>('GET', '/billing/billings', undefined, filters);
  }

  getBilling(id: number): Observable<BillingResponse> {
    return this.api.request<BillingResponse>('GET', `/billing/${id}`);
  }

  createBilling(body: CreateBillingRequest): Observable<CreateBillingResponse> {
    return this.api.request<CreateBillingResponse>('POST', '/billing/billings', body);
  }

  updateBilling(id: number, body: UpdateBillingRequest): Observable<UpdateBillingResponse> {
    return this.api.request<UpdateBillingResponse>('PUT', `/billing/${id}`, body);
  }

  deleteBilling(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/billing/${id}`);
  }
}
