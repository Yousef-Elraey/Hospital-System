import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { AppointmentTimePickerComponent } from '../../../appointments/components/appointment-time-picker/appointment-time-picker.component';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { AppointmentSlotService } from '../../services/time-slot.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { parseDisplayDateToIso, formatDateDisplay } from '../../../../core/utils/display-date';
import { estimateSlotCount } from '../../utils/generate-time-slots';
import { APPOINTMENT_MAX_NGB } from '../../../appointments/utils/appointment-ngb-date';
import { addMonths, format, parseISO } from 'date-fns';
import type { AppointmentType } from '../../../appointments/models/request/appointment-type.dto';
import {
  WEEK_DAYS,
  type GenerateSlotsRequest,
  type WeekDay,
} from '../../models/request/generate-slots-request.dto';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

interface GenerateSlotsForm {
  doctorId: number;
  startDateDisplay: string;
  endDateDisplay: string;
  start: string;
  end: string;
  duration: number;
  days: WeekDay[];
  appointmentType: AppointmentType;
}

@Component({
  selector: 'app-time-slots-generate',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    TranslateModule,
    NgSelectModule,
    PageHeaderComponent,
    RequiredFormLabelComponent,
    HospitalDatepickerComponent,
    AppointmentTimePickerComponent,
  ],
  templateUrl: './time-slots-generate.component.html',
  styleUrls: ['./time-slots-generate.component.css'],
})
export class TimeSlotsGenerateComponent implements OnInit {
  doctorSelectOptions: { value: number; label: string }[] = [];
  slotDurationOptions = [10, 15, 20, 30, 45, 60];
  weekDayOptions: { value: WeekDay; labelKey: string }[] = WEEK_DAYS.map((day) => ({
    value: day,
    labelKey: `appointmentSlots.weekdays.${day}`,
  }));
  appointmentTypeOptions: { value: AppointmentType; labelKey: string }[] = [
    { value: 'INITIAL_CONSULTATION', labelKey: 'appointments.appointmentType.INITIAL_CONSULTATION' },
    { value: 'FOLLOW_UP_CONSULTATION', labelKey: 'appointments.appointmentType.FOLLOW_UP_CONSULTATION' },
  ];
  generateForm: GenerateSlotsForm = {
    doctorId: 0,
    startDateDisplay: '',
    endDateDisplay: '',
    start: '09:00',
    end: '17:00',
    duration: 30,
    days: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
    appointmentType: 'INITIAL_CONSULTATION',
  };

  previewCount = 0;
  generating = false;
  formError = '';

  readonly minDate = new NgbDate(new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate());
  readonly maxDate = APPOINTMENT_MAX_NGB;

  get endMinDate(): NgbDate {
    const startIso = parseDisplayDateToIso(this.generateForm.startDateDisplay, this.locale.currentLang);
    if (!startIso) return this.minDate;
    return this.isoToNgbDate(startIso);
  }

  get endMaxDate(): NgbDate {
    const startIso = parseDisplayDateToIso(this.generateForm.startDateDisplay, this.locale.currentLang);
    if (!startIso) return this.maxDate;
    return this.isoToNgbDate(this.maxEndIsoForStart(startIso));
  }

  private readonly doctorService = inject(DoctorService);
  private readonly slotService = inject(AppointmentSlotService);
  private readonly router = inject(Router);
  readonly locale = inject(LocaleService);

  ngOnInit(): void {
    this.doctorService.getDoctors({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      const doctors = response.data ?? [];
      this.doctorSelectOptions = doctors.map((x) => ({
        value: x.id!,
        label: `${x.name}`,
      }));
    });
  }

  private setDefaultDateRange(): void {
    const today = new Date();
    const end = addMonths(today, 1);
    const lang = this.locale.currentLang;
    this.generateForm.startDateDisplay = formatDateDisplay(format(today, 'yyyy-MM-dd'), lang);
    this.generateForm.endDateDisplay = formatDateDisplay(format(end, 'yyyy-MM-dd'), lang);
    this.onGenerateFormChange();
  }

  isDaySelected(day: WeekDay): boolean {
    return this.generateForm.days.includes(day);
  }

  toggleDay(day: WeekDay, selected: boolean): void {
    if (selected) {
      if (!this.generateForm.days.includes(day)) {
        this.generateForm.days = [...this.generateForm.days, day];
      }
    } else {
      this.generateForm.days = this.generateForm.days.filter((d) => d !== day);
    }
    this.onGenerateFormChange();
  }

  onStartDateChange(): void {
    this.syncEndDateAfterStartChange();
    this.onGenerateFormChange();
  }

  private syncEndDateAfterStartChange(): void {
    const lang = this.locale.currentLang;
    const startIso = parseDisplayDateToIso(this.generateForm.startDateDisplay, lang);
    if (!startIso) return;

    const maxEndIso = this.maxEndIsoForStart(startIso);
    const endIso = parseDisplayDateToIso(this.generateForm.endDateDisplay, lang);
    if (!endIso || endIso < startIso || endIso > maxEndIso) {
      this.generateForm.endDateDisplay = formatDateDisplay(maxEndIso, lang);
    }
  }

  private maxEndIsoForStart(startIso: string): string {
    return format(addMonths(parseISO(startIso), 1), 'yyyy-MM-dd');
  }

  private isoToNgbDate(iso: string): NgbDate {
    const [y, m, d] = iso.split('-').map(Number);
    return new NgbDate(y, m, d);
  }

  onGenerateFormChange(): void {
    this.formError = '';
    const validation = this.validateGenerateForm();
    this.previewCount = validation.valid ? this.buildGenerateRequest().createdCountEstimate : 0;
  }

  private buildGenerateRequest(): GenerateSlotsRequest & { createdCountEstimate: number } {
    const dayStart =
      parseDisplayDateToIso(this.generateForm.startDateDisplay, this.locale.currentLang) ?? '';
    const dayEnd = parseDisplayDateToIso(this.generateForm.endDateDisplay, this.locale.currentLang) ?? '';
    const request: GenerateSlotsRequest = {
      doctorId: this.generateForm.doctorId,
      dayStart,
      dayEnd,
      start: this.generateForm.start,
      end: this.generateForm.end,
      duration: this.generateForm.duration,
      days: this.generateForm.days,
      appointmentType: this.generateForm.appointmentType,
    };
    return { ...request, createdCountEstimate: estimateSlotCount(request) };
  }

  private validateGenerateForm(): { valid: boolean; errorKey?: string } {
    if (!this.generateForm.doctorId) {
      return { valid: false, errorKey: 'appointmentSlots.validation.doctorRequired' };
    }
    const dayStart = parseDisplayDateToIso(this.generateForm.startDateDisplay, this.locale.currentLang);
    const dayEnd = parseDisplayDateToIso(this.generateForm.endDateDisplay, this.locale.currentLang);
    if (!dayStart || !dayEnd) {
      return { valid: false, errorKey: 'appointmentSlots.validation.dateRangeRequired' };
    }
    if (dayEnd < dayStart) {
      return { valid: false, errorKey: 'appointmentSlots.validation.endBeforeStart' };
    }
    if (dayEnd > this.maxEndIsoForStart(dayStart)) {
      return { valid: false, errorKey: 'appointmentSlots.validation.endAfterMaxPeriod' };
    }
    if (!this.generateForm.start || !this.generateForm.end) {
      return { valid: false, errorKey: 'appointmentSlots.validation.timeRangeRequired' };
    }
    const [sh, sm] = this.generateForm.start.split(':').map(Number);
    const [eh, em] = this.generateForm.end.split(':').map(Number);
    if (eh * 60 + em <= sh * 60 + sm) {
      return { valid: false, errorKey: 'appointmentSlots.validation.endTimeBeforeStart' };
    }
    if (!this.slotDurationOptions.includes(this.generateForm.duration)) {
      return { valid: false, errorKey: 'appointmentSlots.validation.durationRequired' };
    }
    if (!this.generateForm.days.length) {
      return { valid: false, errorKey: 'appointmentSlots.validation.daysRequired' };
    }
    return { valid: true };
  }


  generateSlots(form: NgForm): void {
    const validation = this.validateGenerateForm();
    if (!validation.valid) {
      this.formError = validation.errorKey ?? '';
      return;
    }
    if (form.invalid) return;

    const { createdCountEstimate, ...request } = this.buildGenerateRequest();
    if (createdCountEstimate === 0) {
      this.formError = 'appointmentSlots.validation.noSlotsToCreate';
      return;
    }

    this.generating = true;
    this.formError = '';
    this.slotService.generateSlots(request).subscribe({
      next: () => {
        this.generating = false;
        this.router.navigate(['/appointment-slots']);
      },
      error: () => {
        this.generating = false;
      },
    });
  }
}
