import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { UpdateAppointmentRequest } from '../../models/request/update-appointment-request.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { combineAppointmentIsoDateAndTime, dateToIsoDateString, parseAppointmentTiming } from '../../utils/date-form';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { LocaleService } from '../../../../core/services/locale.service';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { APPOINTMENT_MAX_NGB } from '../../utils/appointment-ngb-date';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-appointments-edit',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    RouterLink,
    PageHeaderComponent,
    HospitalDatepickerComponent,
    NgSelectModule,
  ],
  templateUrl: './appointments-edit.component.html',
  styleUrls: ['./appointments-edit.component.css'],
})
export class AppointmentsEditComponent implements OnInit {
  readonly apptMinNgb = (() => {
    const now = new Date();
    return new NgbDate(now.getFullYear(), now.getMonth() + 1, now.getDate());
  })();
  readonly apptMaxNgb = APPOINTMENT_MAX_NGB;

  id: number | null = null;
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  model: UpdateAppointmentRequest = { timing: '', doctorId: 0, patientId: 0 };
  apptDateStr = '';
  apptTime = '';
  apptHour12: number | null = null;
  apptMinute: number | null = null;
  apptMeridian: 'AM' | 'PM' = 'AM';
  readonly hourOptions = Array.from({ length: 12 }, (_, i) => i + 1);
  readonly minuteOptions = [0, 10, 20, 30, 40, 50];
  loading = false;

  constructor(
    private appointmentService: AppointmentService, private doctorService: DoctorService, private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.doctorService.getDoctors({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.doctors = response.data ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({
        value: x.id!,
        label: `${x.name} `,
      }));
    });
    this.patientService.getPatients({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.patients = response.data ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.appointmentService.getAppointment(this.id).subscribe({
      next: (data) => {
        this.model = {
          timing: data.timing,
          doctorId: data.doctorId,
          patientId: data.patientId,
          status: data.status,
        };
        const p = parseAppointmentTiming(this.model.timing);
        this.apptDateStr = p.date ? dateToIsoDateString(p.date) : '';
        this.apptTime = p.time;
        this.applyTimePartsFrom24(this.apptTime);
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    this.syncTime24();
    if (form.invalid || !this.id) return;
    const iso = parseDisplayDateToIso(this.apptDateStr, this.locale.currentLang);
    if (!iso) return;
    this.model.timing = combineAppointmentIsoDateAndTime(iso, this.apptTime);
    this.appointmentService.updateAppointment(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/appointments', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/appointments', this.id]);
  }

  onTimePartChange(): void {
    this.syncTime24();
  }

  formatLocalizedNumber(value: number): string {
    const twoDigits = String(value).padStart(2, '0');
    if (this.locale.currentLang === 'ar') {
      return twoDigits.replace(/\d/g, (digit) => '٠١٢٣٤٥٦٧٨٩'[Number(digit)]);
    }
    return twoDigits;
  }

  private applyTimePartsFrom24(time: string): void {
    const match = /^(\d{2}):(\d{2})$/.exec(time);
    if (!match) {
      this.apptHour12 = null;
      this.apptMinute = null;
      this.apptMeridian = 'AM';
      return;
    }
    const hour24 = Number(match[1]);
    const minute = Number(match[2]);
    this.apptMeridian = hour24 >= 12 ? 'PM' : 'AM';
    const hour12 = hour24 % 12 || 12;
    this.apptHour12 = hour12;
    this.apptMinute = Math.floor(minute / 10) * 10;
  }

  private syncTime24(): void {
    if (this.apptHour12 == null || this.apptMinute == null) {
      this.apptTime = '';
      return;
    }
    const hour12 = this.apptHour12;
    const minute = this.apptMinute;
    let hour24 = hour12 % 12;
    if (this.apptMeridian === 'PM') hour24 += 12;
    this.apptTime = `${String(hour24).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
  }
}
