import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { BillingService } from '../../services/billing.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { BillingResponse } from '../../models/response/billing-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { NgSelectModule } from '@ng-select/ng-select';
import { isTodayFromIso } from '../../../../core/utils/is-today';
import {
  DEFAULT_PAGE_SIZE_OPTIONS,
  DROPDOWN_FETCH_SIZE,
  STATS_FETCH_SIZE,
  applyPageResponse,
  toPageRequest,
} from '../../../../core/utils/list-pagination';

interface BillingStats {
  total: number;
  today: number;
}

@Component({
  selector: 'app-billing-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, DecimalPipe, PageHeaderComponent, IconComponent, ListFilterToggleComponent, ListPaginationComponent, NgSelectModule],
  templateUrl: './billing-list.component.html',
  styleUrls: ['./billing-list.component.css'],
})
export class BillingListComponent implements OnInit {
  list: BillingResponse[] = [];
  totalElements = 0;
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  statsLoading = false;
  showFilters = false;
  filters = { patient_id: null as number | null, amount: null as number | null };
  stats: BillingStats = { total: 0, today: 0 };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

  constructor(
    private billingService: BillingService,
    private patientService: PatientService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.loadStats();
    this.load();
    this.patientService.getPatients({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.patients = response.data ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  loadStats(): void {
    this.statsLoading = true;
    this.billingService.getBillings({ page: 0, size: STATS_FETCH_SIZE }).subscribe({
      next: (response) => {
        const billings = response.data ?? [];
        let today = 0;
        for (const billing of billings) {
          if (isTodayFromIso(billing.createdAt)) today++;
        }
        this.stats = { total: response.totalElements ?? billings.length, today };
        this.statsLoading = false;
      },
      error: () => { this.statsLoading = false; },
    });
  }

  load(): void {
    this.loading = true;
    this.billingService.getBillings({
      patient_id: this.filters.patient_id ?? undefined,
      amount: this.filters.amount ?? undefined,
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
    this.filters = { patient_id: null, amount: null };
    this.currentPage = 1;
    this.load();
  }

  delete(b: BillingResponse): void {
    if (!b.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteBilling' })
      .subscribe((ok) => {
        if (!ok) return;
        this.billingService.deleteBilling(b.id!).subscribe({
          next: () => {
            this.loadStats();
            this.load();
          },
        });
      });
  }

  patientName(patientId: number): string {
    const p = this.patients.find((x) => x.id === patientId);
    return p ? p.name : String(patientId);
  }
}
