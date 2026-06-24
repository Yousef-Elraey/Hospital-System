import { Component, OnInit } from '@angular/core';
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
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { NgSelectModule } from '@ng-select/ng-select';

@Component({
  selector: 'app-appointments-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, IconComponent, NgSelectModule],
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
  showVisitTypeDialog = false;
  visitTypeChoice: 'NEW' | 'EXISTING' = 'NEW';
  existingMode = false;
  existingPhone = '';
  existingLoading = false;
  existingError = '';
  filters = { doctorId: null as number | null, patientId: null as number | null, status: '', date: '' };

  constructor(
    private appointmentService: AppointmentService, private doctorService: DoctorService, private patientService: PatientService,
    private router: Router,
    private confirmDialog: ConfirmDialogService,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
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

  load(): void {
    this.loading = true;
    const dateIso = parseDisplayDateToIso(this.filters.date, this.locale.currentLang) ?? undefined;
    this.appointmentService.getAppointments({
      doctorId: this.filters.doctorId ?? undefined,
      patientId: this.filters.patientId ?? undefined,
      status: this.filters.status.trim() || undefined,
      date: dateIso,
    }).subscribe({
      next: (data) => { this.list = data ?? []; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  applyFilters(): void {
    this.load();
  }

  clearFilters(): void {
    this.filters = { doctorId: null, patientId: null, status: '', date: '' };
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
          next: () => this.load(),
        });
      });
  }

  formatTiming(t: string | undefined): string {
    return formatDateTimeDisplay(t, this.locale.currentLang);
  }
}
