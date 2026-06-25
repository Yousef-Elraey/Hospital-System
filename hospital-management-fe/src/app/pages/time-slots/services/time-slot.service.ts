import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import type { PageResponse } from '../../../core/models/pagination.dto';
import type { AppointmentSlotResponse } from '../models/response/appointment-slot-response.dto';
import type { GenerateSlotsRequest } from '../models/request/generate-slots-request.dto';
import type { GenerateSlotsResponse } from '../models/response/generate-slots-response.dto';
import type { AppointmentSlotFilters } from '../models/request/appointment-slot-filters.dto';
import { generateTimeSlots } from '../utils/generate-time-slots';

/**
 * Appointment slot management service.
 *
 * Backend is not ready yet — all methods use in-memory dummy data below.
 * When the API is available, replace each method body with ApiHttpService calls
 * (see commented examples) and remove the dummy store.
 */
@Injectable({ providedIn: 'root' })
export class AppointmentSlotService {
  // --- Dummy data (remove when wiring the real API) ---
  private nextId = 4;
  private dummySlots: AppointmentSlotResponse[] = [
    {
      id: 1,
      doctorId: 1,
      startTime: '2026-06-09T09:00',
      endTime: '2026-06-09T09:30',
      status: 'AVAILABLE',
      createdAt: '2026-06-08T10:00:00',
    },
    {
      id: 2,
      doctorId: 1,
      startTime: '2026-06-09T09:30',
      endTime: '2026-06-09T10:00',
      status: 'BOOKED',
      createdAt: '2026-06-08T10:00:00',
    },
    {
      id: 3,
      doctorId: 2,
      startTime: '2026-06-10T14:00',
      endTime: '2026-06-10T14:20',
      status: 'AVAILABLE',
      createdAt: '2026-06-08T11:00:00',
    },
  ];

  getSlots(filters?: AppointmentSlotFilters): Observable<PageResponse<AppointmentSlotResponse>> {
    // return this.api.request<PageResponse<AppointmentSlotResponse>>('GET', '/appointment/slots', undefined, filters);
    let result = [...this.dummySlots];
    if (filters?.doctorId != null) {
      result = result.filter((s) => s.doctorId === filters.doctorId);
    }
    if (filters?.status) {
      result = result.filter((s) => s.status === filters.status);
    }
    if (filters?.startDate) {
      result = result.filter((s) => s.startTime.slice(0, 10) >= filters.startDate!);
    }
    if (filters?.endDate) {
      result = result.filter((s) => s.startTime.slice(0, 10) <= filters.endDate!);
    }
    result.sort((a, b) => a.startTime.localeCompare(b.startTime));

    const page = filters?.page ?? 0;
    const size = filters?.size ?? 10;
    const totalElements = result.length;
    const totalPages = Math.max(1, Math.ceil(totalElements / size));
    const data = result.slice(page * size, (page + 1) * size);

    return of({
      data,
      page,
      size,
      totalElements,
      totalPages,
      first: page === 0,
      last: page >= totalPages - 1,
    }).pipe(delay(200));
  }

  generateSlots(body: GenerateSlotsRequest): Observable<GenerateSlotsResponse> {
    // return this.api.request<GenerateSlotsResponse>('POST', '/appointment/slots/generate', body);
    const drafts = generateTimeSlots(body);
    const existingKeys = new Set(
      this.dummySlots
        .filter((s) => s.doctorId === body.doctorId)
        .map((s) => s.startTime),
    );

    const created: AppointmentSlotResponse[] = [];
    const now = new Date().toISOString();
    for (const draft of drafts) {
      if (existingKeys.has(draft.startTime)) continue;
      const slot: AppointmentSlotResponse = {
        ...draft,
        id: this.nextId++,
        createdAt: now,
      };
      this.dummySlots.push(slot);
      existingKeys.add(draft.startTime);
      created.push(slot);
    }

    return of({ createdCount: created.length, slots: created }).pipe(delay(300));
  }

  deleteSlot(id: number): Observable<string> {
    // return this.api.request<string>('DELETE', `/appointment/slots/${id}`);
    const index = this.dummySlots.findIndex((s) => s.id === id);
    if (index >= 0) {
      this.dummySlots.splice(index, 1);
    }
    return of('deleted').pipe(delay(200));
  }

  deleteSlotsByDoctor(doctorId: number, startDate: string, endDate: string): Observable<string> {
    // return this.api.request<string>('DELETE', `/appointment/slots/bulk`, { doctorId, startDate, endDate });
    this.dummySlots = this.dummySlots.filter(
      (s) =>
        s.doctorId !== doctorId ||
        s.startTime.slice(0, 10) < startDate ||
        s.startTime.slice(0, 10) > endDate,
    );
    return of('deleted').pipe(delay(200));
  }
}
