import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { REPORTS_MOCK_DATA } from '../data/reports-mock.data';
import type { ReportsDashboard } from '../models/reports-dashboard.dto';

@Injectable({ providedIn: 'root' })
export class ReportsService {
  getDashboard(): Observable<ReportsDashboard> {
    return of(REPORTS_MOCK_DATA).pipe(delay(300));
  }
}
