import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { AppointmentSlotService } from '../../services/time-slot.service';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { formatDateTimeDisplay } from '../../../appointments/utils/date-form';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { AppointmentSlotResponse } from '../../models/response/appointment-slot-response.dto';
import type { AppointmentSlotStatus } from '../../models/request/appointment-slot-status.dto';
import { DEFAULT_PAGE_SIZE_OPTIONS, DROPDOWN_FETCH_SIZE, applyPageResponse, toPageRequest } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-time-slots-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    TranslateModule,
    NgSelectModule,
    PageHeaderComponent,
    ListFilterToggleComponent,
    ListPaginationComponent,
  ],
  templateUrl: './time-slots-list.component.html',
  styleUrls: ['./time-slots-list.component.css'],
})
export class TimeSlotsListComponent implements OnInit {
  doctors: DoctorResponse[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  statusOptions: AppointmentSlotStatus[] = ['AVAILABLE', 'BOOKED', 'BLOCKED'];

  listFilters = {
    doctorId: null as number | null,
    status: null as AppointmentSlotStatus | null,
    startDate: '',
    endDate: '',
  };

  slots: AppointmentSlotResponse[] = [];
  totalElements = 0;
  loading = false;
  showFilters = false;
  errorMessage = '';
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

  private readonly doctorService = inject(DoctorService);
  private readonly slotService = inject(AppointmentSlotService);
  private readonly confirmDialog = inject(ConfirmDialogService);
  readonly locale = inject(LocaleService);

  ngOnInit(): void {
    this.doctorService.getDoctors({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.doctors = response.data ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({
        value: x.id!,
        label: `${x.name}`,
      }));
    });
    this.loadSlots();
  }

  loadSlots(): void {
    this.loading = true;
    this.errorMessage = '';
    const startIso = parseDisplayDateToIso(this.listFilters.startDate, this.locale.currentLang) ?? undefined;
    const endIso = parseDisplayDateToIso(this.listFilters.endDate, this.locale.currentLang) ?? undefined;
    this.slotService
      .getSlots({
        doctorId: this.listFilters.doctorId ?? undefined,
        status: this.listFilters.status ?? undefined,
        startDate: startIso,
        endDate: endIso,
        ...toPageRequest(this.currentPage, this.pageSize),
      })
      .subscribe({
        next: (response) => {
          const page = applyPageResponse(response, { pageSize: this.pageSize });
          this.slots = page.list;
          this.totalElements = page.totalElements;
          this.currentPage = page.currentPage;
          this.pageSize = page.pageSize;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        },
      });
  }

  setPage(page: number): void {
    this.currentPage = page;
    this.loadSlots();
  }

  setPageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
    this.loadSlots();
  }

  applyListFilters(): void {
    this.currentPage = 1;
    this.loadSlots();
  }

  clearListFilters(): void {
    this.listFilters = { doctorId: null, status: null, startDate: '', endDate: '' };
    this.currentPage = 1;
    this.loadSlots();
  }

  deleteSlot(slot: AppointmentSlotResponse): void {
    if (!slot.id) return;
    if (slot.status === 'BOOKED') {
      this.errorMessage = 'appointmentSlots.cannotDeleteBooked';
      return;
    }
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteSlot' })
      .subscribe((ok) => {
        if (!ok) return;
        this.slotService.deleteSlot(slot.id).subscribe({
          next: () => this.loadSlots(),
        });
      });
  }

  doctorName(doctorId: number): string {
    return this.doctors.find((d) => d.id === doctorId)?.name ?? String(doctorId);
  }

  formatSlotTime(iso: string | undefined): string {
    return formatDateTimeDisplay(iso, this.locale.currentLang);
  }

  statusBadgeClass(status: AppointmentSlotStatus): string {
    switch (status) {
      case 'AVAILABLE':
        return 'bg-success-subtle text-success-emphasis';
      case 'BOOKED':
        return 'bg-primary-subtle text-primary-emphasis';
      case 'BLOCKED':
        return 'bg-secondary-subtle text-secondary-emphasis';
      default:
        return 'bg-light text-body';
    }
  }
}
