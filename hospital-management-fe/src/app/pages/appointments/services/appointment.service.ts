import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import type { AppointmentResponse } from '../models/response/appointment-response.dto';
import type { CreateAppointmentRequest } from '../models/request/create-appointment-request.dto';
import type { UpdateAppointmentRequest } from '../models/request/update-appointment-request.dto';
import type { CreateAppointmentResponse } from '../models/response/create-appointment-response.dto';
import type { UpdateAppointmentResponse } from '../models/response/update-appointment-response.dto';
import type { AppointmentFilters } from '../models/request/appointment-filters.dto';
import type { BookAppointmentRequest } from '../models/request/book-appointment-request.dto';
import type { BookAppointmentResponse } from '../models/response/book-appointment-response.dto';
import type { PatientResponse } from '../../patients/models/response/patient-response.dto';

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  constructor(private api: ApiHttpService) {}

  getAppointments(filters?: AppointmentFilters): Observable<AppointmentResponse[]> {
    return this.api.request<AppointmentResponse[]>('GET', '/appointment/appointments', undefined, filters);
  }

  getAppointment(id: number): Observable<AppointmentResponse> {
    return this.api.request<AppointmentResponse>('GET', `/appointment/${id}`);
  }

  createAppointment(body: CreateAppointmentRequest): Observable<CreateAppointmentResponse> {
    return this.api.request<CreateAppointmentResponse>('POST', '/appointment/appointments', body);
  }

  updateAppointment(id: number, body: UpdateAppointmentRequest): Observable<UpdateAppointmentResponse> {
    return this.api.request<UpdateAppointmentResponse>('PUT', `/appointment/${id}`, body);
  }

  deleteAppointment(id: number): Observable<string> {
    return this.api.request<string>('DELETE', `/appointment/${id}`);
  }

  bookAppointment(body: BookAppointmentRequest): Observable<BookAppointmentResponse> {
    return this.api.request<BookAppointmentResponse>('POST', '/appointment/book', body);
  }

  bookAppointmentWithPaid(body: BookAppointmentRequest): Observable<BookAppointmentResponse> {
    return this.api.request<BookAppointmentResponse>('POST', '/appointment/book-with-paid', body);
  }

  currentPatient(): Observable<PatientResponse> {
    return this.api.request<PatientResponse>('GET', '/appointment/current-patient');
  }

  nextPatient(): Observable<PatientResponse> {
    return this.api.request<PatientResponse>('DELETE', '/appointment/next');
  }
}
