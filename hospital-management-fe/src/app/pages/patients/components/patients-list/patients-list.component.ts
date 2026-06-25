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
import {
  clampPage,
  DEFAULT_PAGE_SIZE_OPTIONS,
  paginate,
} from '../../../../core/utils/list-pagination';

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
  ],
  templateUrl: './patients-list.component.html',
  styleUrls: ['./patients-list.component.css'],
})
export class PatientsListComponent implements OnInit {
  list: PatientResponse[] = [];
  loading = false;
  statsLoading = false;
  showFilters = false;
  filters = { name: '', dateOfBirth: '', phone: '' };
  stats: PatientStats = { total: 0, today: 0 };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
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
    this.patientService.getPatients().subscribe({
      next: (data) => {
        this.stats = this.computeStats(data ?? []);
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
    }).subscribe({
      next: (data) => {
        this.list = data ?? [];
        this.loading = false;
        this.currentPage = clampPage(this.currentPage, this.list.length, this.pageSize);
      },
      error: () => { this.loading = false; },
    });
  }

  get pagedList(): PatientResponse[] {
    return paginate(this.list, this.currentPage, this.pageSize);
  }

  setPage(page: number): void {
    this.currentPage = page;
  }

  setPageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
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
