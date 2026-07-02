import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { PatientService } from '../../services/patient.service';
import type { PatientResponse } from '../../models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { isTodayFromIso } from '../../../../core/utils/is-today';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { computePatientAge } from '../../utils/compute-patient-age';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { DEFAULT_PAGE_SIZE_OPTIONS, applyPageResponse, STATS_FETCH_SIZE, toPageRequest } from '../../../../core/utils/list-pagination';

interface PatientStats {
  total: number;
  today: number;
}

@Component({
  selector: 'app-patients-list',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    TranslateModule,
    DecimalPipe,
    PageHeaderComponent,
    IconComponent,
    ListFilterToggleComponent,
    ListPaginationComponent,
    HospitalDatepickerComponent,
  ],
  templateUrl: './patients-list.component.html',
  styleUrls: ['./patients-list.component.css'],
})
export class PatientsListComponent implements OnInit {
  list: PatientResponse[] = [];
  totalElements = 0;
  loading = false;
  statsLoading = false;
  showFilters = false;
  filters = { name: '', dateOfBirth: '', phone: '' };
  stats: PatientStats = { total: 0, today: 0 };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  readonly filterMaxDate = (() => {
    const now = new Date();
    return new NgbDate(now.getFullYear(), now.getMonth() + 1, now.getDate());
  })();
  pageSize = 10;
  currentPage = 1;

  constructor(
    private patientService: PatientService,
    private confirmDialog: ConfirmDialogService,
    private locale: LocaleService,
  ) {}

  ngOnInit(): void {
    this.loadStats();
    this.load();
  }

  loadStats(): void {
    this.statsLoading = true;
    this.patientService.getPatients({ page: 0, size: STATS_FETCH_SIZE }).subscribe({
      next: (response) => {
        const data = response.data ?? [];
        this.stats = this.computeStats(data);
        this.stats.total = response.totalElements ?? data.length;
        this.statsLoading = false;
      },
      error: () => { this.statsLoading = false; },
    });
  }

  private computeStats(patients: PatientResponse[]): PatientStats {
    let today = 0;
    for (const p of patients) {
      if (p.createdAt && isTodayFromIso(p.createdAt)) today++;
    }
    return { total: patients.length, today };
  }

  load(): void {
    this.loading = true;
    const dateOfBirthIso = parseDisplayDateToIso(this.filters.dateOfBirth, this.locale.currentLang) ?? undefined;
    this.patientService.getPatients({
      name: this.filters.name.trim() || undefined,
      dateOfBirth: dateOfBirthIso,
      phone: this.filters.phone.trim() || undefined,
      mobile: this.filters.phone.trim() || undefined,
      ...toPageRequest(this.currentPage, this.pageSize),
    }).subscribe({
      next: (response) => {
        const page = applyPageResponse(response, { pageSize: this.pageSize });
        this.list = page.list;
        this.totalElements = page.totalElements;
        this.currentPage = page.currentPage;
        this.pageSize = page.pageSize;
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  setPage(page: number): void {
    this.currentPage = page;
    this.load();
  }

  setPageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
    this.load();
  }

  applyFilters(): void {
    this.currentPage = 1;
    this.load();
  }

  clearFilters(): void {
    this.filters = { name: '', dateOfBirth: '', phone: '' };
    this.currentPage = 1;
    this.load();
  }

  formatAge(value: string | undefined): string {
    const age = computePatientAge(value);
    return age == null ? '-' : String(age);
  }

  delete(p: PatientResponse): void {
    if (!p.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deletePatient' })
      .subscribe((ok) => {
        if (!ok) return;
        this.patientService.deletePatient(p.id!).subscribe({
          next: () => {
            this.loadStats();
            this.load();
          },
        });
      });
  }
}
