import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { AppointmentType } from '../../models/request/appointment-type.dto';
import type { BookAppointmentRequest } from '../../models/request/book-appointment-request.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { combineAppointmentIsoDateAndTime } from '../../utils/date-form';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { LocaleService } from '../../../../core/services/locale.service';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { APPOINTMENT_MAX_NGB } from '../../utils/appointment-ngb-date';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { AppointmentDetailsFormComponent } from '../appointment-details-form/appointment-details-form.component';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-appointments-add',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    PageHeaderComponent,
    HospitalDatepickerComponent,
    NgSelectModule,
    AppointmentDetailsFormComponent,
  ],
  templateUrl: './appointments-add.component.html',
  styleUrls: ['./appointments-add.component.css'],
})
export class AppointmentsAddComponent implements OnInit {
  readonly apptMinNgb = (() => {
    const now = new Date();
    return new NgbDate(now.getFullYear(), now.getMonth() + 1, now.getDate());
  })();
  readonly apptMaxNgb = APPOINTMENT_MAX_NGB;

  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  model: {
    doctorId: number | null;
    patientId: number | null;
    appointmentType: AppointmentType;
  } = { doctorId: null, patientId: null, appointmentType: 'INITIAL_CONSULTATION' };
  apptDateStr = '';
  apptTime = '';
  selectedPatient: { id: number; name: string; phone: string } | null = null;

  constructor(
    private appointmentService: AppointmentService, private doctorService: DoctorService, private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    const qp = this.route.snapshot.queryParamMap;
    const idStr = qp.get('patientId');
    const id = idStr ? Number(idStr) : null;
    if (id && !Number.isNaN(id)) {
      this.selectedPatient = {
        id,
        name: qp.get('patientName') ?? '',
        phone: qp.get('patientPhone') ?? '',
      };
      this.model.patientId = id;
    }
    this.doctorService.getDoctors({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.doctors = response.data ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({
        value: x.id!,
        label: `${x.name}`,
      }));
    });
    this.patientService.getPatients({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.patients = response.data ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  bookWithoutPaid(form: NgForm): void {
    this.bookInternal(form, false);
  }

  bookWithPaid(form: NgForm): void {
    this.bookInternal(form, true);
  }

  private bookInternal(form: NgForm, paid: boolean): void {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }
    const iso = parseDisplayDateToIso(this.apptDateStr, this.locale.currentLang);
    if (!iso) return;
    const timing = combineAppointmentIsoDateAndTime(iso, this.apptTime);
    const request: BookAppointmentRequest = {
      patientId: this.model.patientId ?? undefined,
      doctorId: this.model.doctorId!,
      appointmentTiming: timing,
      appointmentType: this.model.appointmentType,
      statusId: 'PENDING',
    };
    const call = paid ? this.appointmentService.bookAppointmentWithPaid(request) : this.appointmentService.bookAppointment(request);
    call.subscribe({
      next: () => this.router.navigate(['/appointments']),
    });
  }

  cancel(): void {
    this.router.navigate(['/appointments']);
  }
}
