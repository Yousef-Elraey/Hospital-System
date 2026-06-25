import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { formatDateTimeDisplay } from '../../utils/date-form';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import type { AppointmentResponse } from '../../models/response/appointment-response.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { NgSelectModule } from '@ng-select/ng-select';
import {
  computeAppointmentTodayStats,
  type AppointmentTodayStats,
} from '../../utils/appointment-today-stats';
import {
  clampPage,
  DEFAULT_PAGE_SIZE_OPTIONS,
  paginate,
} from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-appointments-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, DecimalPipe, PageHeaderComponent, ListFilterToggleComponent, ListPaginationComponent, IconComponent, NgSelectModule],
  templateUrl: './appointments-list.component.html',
  styleUrls: ['./appointments-list.component.css'],
})
export class AppointmentsListComponent implements OnInit {
  list: AppointmentResponse[] = [];
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  statsLoading = false;
  showFilters = false;
  showVisitTypeDialog = false;
  visitTypeChoice: 'NEW' | 'EXISTING' = 'NEW';
  existingMode = false;
  existingPhone = '';
  existingLoading = false;
  existingError = '';
  filters = { doctorId: null as number | null, patientId: null as number | null, status: '', date: '' };
  stats: AppointmentTodayStats = { booked: 0, paid: 0, unpaid: 0, waiting: 0, completed: 0 };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

  constructor(
    private appointmentService: AppointmentService, private doctorService: DoctorService, private patientService: PatientService,
    private router: Router,
    private confirmDialog: ConfirmDialogService,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    this.loadStats();
    this.load();
    this.doctorService.getDoctors().subscribe((d) => {
      this.doctors = d ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: `${x.name} (${x.speciality})` }));
    });
    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  loadStats(): void {
    this.statsLoading = true;
    this.appointmentService.getAppointments().subscribe({
      next: (data) => {
        this.stats = computeAppointmentTodayStats(data ?? []);
        this.statsLoading = false;
      },
      error: () => { this.statsLoading = false; },
    });
  }

  load(): void {
    this.loading = true;
    const dateIso = parseDisplayDateToIso(this.filters.date, this.locale.currentLang) ?? undefined;
    this.appointmentService.getAppointments({
      doctorId: this.filters.doctorId ?? undefined,
      patientId: this.filters.patientId ?? undefined,
      status: this.filters.status.trim() || undefined,
      date: dateIso,
    }).subscribe({
      next: (data) => {
        this.list = data ?? [];
        this.loading = false;
        this.currentPage = clampPage(this.currentPage, this.list.length, this.pageSize);
      },
      error: () => { this.loading = false; },
    });
  }

  get pagedList(): AppointmentResponse[] {
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
    this.filters = { doctorId: null, patientId: null, status: '', date: '' };
    this.currentPage = 1;
    this.load();
  }

  openBookingFlowDialog(): void {
    this.visitTypeChoice = 'NEW';
    this.showVisitTypeDialog = true;
    this.existingMode = false;
    this.existingPhone = '';
    this.existingError = '';
  }

  closeBookingFlowDialog(): void {
    this.showVisitTypeDialog = false;
    this.existingMode = false;
  }

  confirmBookingFlowSelection(): void {
    this.showVisitTypeDialog = false;
    const target = this.visitTypeChoice === 'NEW' ? '/appointments/book' : '/appointments/add';
    this.router.navigate([target]);
  }

  selectExistingPatient(): void {
    this.visitTypeChoice = 'EXISTING';
    this.existingMode = true;
    this.existingError = '';
  }

  searchExistingPatient(): void {
    const phone = this.existingPhone.trim();
    if (!/^01[0-9]{9}$/.test(phone)) {
      this.existingError = 'validation.phoneFormat';
      return;
    }
    this.existingLoading = true;
    this.existingError = '';
    this.patientService.getPatientByPhone(phone).subscribe({
      next: (p) => {
        this.existingLoading = false;
        if (!p?.id) {
          this.existingError = 'appointments.patientNotFound';
          return;
        }
        this.showVisitTypeDialog = false;
        this.router.navigate(['/appointments/add'], {
          queryParams: { patientId: p.id, patientName: p.name, patientPhone: p.phone },
        });
      },
      error: () => {
        this.existingLoading = false;
        this.existingError = 'appointments.patientNotFound';
      },
    });
  }

  delete(a: AppointmentResponse): void {
    if (!a.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteAppointment' })
      .subscribe((ok) => {
        if (!ok) return;
        this.appointmentService.deleteAppointment(a.id!).subscribe({
          next: () => {
            this.loadStats();
            this.load();
          },
        });
      });
  }

  formatTiming(t: string | undefined): string {
    return formatDateTimeDisplay(t, this.locale.currentLang);
  }
}
