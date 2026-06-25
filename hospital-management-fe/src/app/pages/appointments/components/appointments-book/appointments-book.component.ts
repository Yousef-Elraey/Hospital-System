import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { BookAppointmentRequest } from '../../models/request/book-appointment-request.dto';
import type { CreatePatientRequest } from '../../../patients/models/request/create-patient-request.dto';
import type { CreatePatientResponse } from '../../../patients/models/response/create-patient-response.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { combineAppointmentIsoDateAndTime } from '../../utils/date-form';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { LocaleService } from '../../../../core/services/locale.service';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { APPOINTMENT_MAX_NGB } from '../../utils/appointment-ngb-date';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { PatientFormFieldsComponent } from '../../../patients/components/patient-form-fields/patient-form-fields.component';
import { switchMap } from 'rxjs';
import { AppointmentDetailsFormComponent } from '../appointment-details-form/appointment-details-form.component';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-appointments-book',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    PageHeaderComponent,
    PatientFormFieldsComponent,
    HospitalDatepickerComponent,
    NgSelectModule,
    AppointmentDetailsFormComponent,
  ],
  templateUrl: './appointments-book.component.html',
  styleUrls: ['./appointments-book.component.css'],
})
export class AppointmentsBookComponent implements OnInit {
  private readonly phoneRegex = /^01[0-9]{9}$/;
  readonly apptMinNgb = (() => {
    const now = new Date();
    return new NgbDate(now.getFullYear(), now.getMonth() + 1, now.getDate());
  })();
  readonly apptMaxNgb = APPOINTMENT_MAX_NGB;

  doctors: DoctorResponse[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  newPatientModel: CreatePatientRequest = { name: '', gender: '', phone: '', dateOfBirth: '' };
  bookModel: Partial<BookAppointmentRequest> = {
    patientName: '',
    patientGender: '',
    patientPhone: '',
    patientDateOfBirth: '',
    appointmentTiming: '',
    appointmentType: 'INITIAL_CONSULTATION',
    doctorId: null as unknown as number,
    statusId: 'PENDING',
  };
  currentStep = 1;
  newPatientDobDateStr = '';
  bookApptDateStr = '';
  bookApptTime = '';

  constructor(
    private appointmentService: AppointmentService, private doctorService: DoctorService, private patientService: PatientService,
    private router: Router,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    this.doctorService.getDoctors({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.doctors = response.data ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: x.name }));
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
    const dobIso = parseDisplayDateToIso(this.newPatientDobDateStr, this.locale.currentLang);
    if (!dobIso) return;
    this.bookModel.patientName = this.newPatientModel.name ?? '';
    this.bookModel.patientGender = this.newPatientModel.gender ?? '';
    this.bookModel.patientPhone = this.newPatientModel.phone ?? '';
    this.bookModel.patientDateOfBirth = dobIso;
    const iso = parseDisplayDateToIso(this.bookApptDateStr, this.locale.currentLang);
    if (!iso) return;
    this.bookModel.appointmentTiming = combineAppointmentIsoDateAndTime(iso, this.bookApptTime);
    const createPatientRequest: CreatePatientRequest = {
      name: this.bookModel.patientName ?? '',
      gender: this.bookModel.patientGender ?? '',
      phone: this.bookModel.patientPhone ?? '',
      dateOfBirth: this.bookModel.patientDateOfBirth,
    };
    this.patientService
      .addPatient(createPatientRequest)
      .pipe(
        switchMap((response: CreatePatientResponse) => this.bookWithPatientId(response.id, paid)),
      )
      .subscribe({
        next: () => this.router.navigate(['/appointments']),
      });
  }

  cancel(): void {
    this.router.navigate(['/appointments']);
  }

  goToStep(step: number): void {
    this.currentStep = step;
  }

  isPatientStepValid(): boolean {
    const name = (this.newPatientModel.name ?? '').trim();
    const gender = (this.newPatientModel.gender ?? '').trim();
    const phone = (this.newPatientModel.phone ?? '').trim();
    const dobIso = parseDisplayDateToIso(this.newPatientDobDateStr, this.locale.currentLang);
    return !!name && !!gender && !!dobIso && this.phoneRegex.test(phone);
  }

  private bookWithPatientId(patientId: number, paid: boolean) {
    const request: BookAppointmentRequest = {
      patientId,
      appointmentTiming: this.bookModel.appointmentTiming ?? '',
      appointmentType: this.bookModel.appointmentType,
      doctorId: this.bookModel.doctorId as number,
      statusId: this.bookModel.statusId,
    };
    return paid ? this.appointmentService.bookAppointmentWithPaid(request) : this.appointmentService.bookAppointment(request);
  }
}
