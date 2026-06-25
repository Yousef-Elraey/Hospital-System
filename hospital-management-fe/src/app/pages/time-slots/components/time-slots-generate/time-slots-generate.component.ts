import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { AppointmentTimePickerComponent } from '../../../appointments/components/appointment-time-picker/appointment-time-picker.component';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { AppointmentSlotService } from '../../services/time-slot.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { estimateSlotCount } from '../../utils/generate-time-slots';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { GenerateSlotsRequest } from '../../models/request/generate-slots-request.dto';

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
    HospitalDatepickerComponent,
    AppointmentTimePickerComponent,
  ],
  templateUrl: './time-slots-generate.component.html',
  styleUrls: ['./time-slots-generate.component.css'],
})
export class TimeSlotsGenerateComponent implements OnInit {
  doctorSelectOptions: { value: number; label: string }[] = [];
  slotDurationOptions = [10, 15, 20, 30, 45, 60];

  generateForm: GenerateSlotsRequest & { startDateDisplay: string; endDateDisplay: string } = {
    doctorId: 0,
    startDate: '',
    endDate: '',
    startDateDisplay: '',
    endDateDisplay: '',
    dailyStartTime: '09:00',
    dailyEndTime: '17:00',
    slotDurationMinutes: 30,
    excludeWeekends: true,
  };

  previewCount = 0;
  generating = false;
  formError = '';

  readonly minDate = new NgbDate(new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate());

  private readonly doctorService = inject(DoctorService);
  private readonly slotService = inject(AppointmentSlotService);
  private readonly router = inject(Router);
  readonly locale = inject(LocaleService);

  ngOnInit(): void {
    this.doctorService.getDoctors().subscribe((d) => {
      const doctors = (d ?? []) as DoctorResponse[];
      this.doctorSelectOptions = doctors.map((x) => ({
        value: x.id!,
        label: `${x.name} (${x.speciality})`,
      }));
    });
  }

  onGenerateFormChange(): void {
    this.formError = '';
    const validation = this.validateGenerateForm();
    this.previewCount = validation.valid ? this.buildGenerateRequest().createdCountEstimate : 0;
  }

  private buildGenerateRequest(): GenerateSlotsRequest & { createdCountEstimate: number } {
    const startIso =
      parseDisplayDateToIso(this.generateForm.startDateDisplay, this.locale.currentLang) ?? '';
    const endIso = parseDisplayDateToIso(this.generateForm.endDateDisplay, this.locale.currentLang) ?? '';
    const request: GenerateSlotsRequest = {
      doctorId: this.generateForm.doctorId,
      startDate: startIso,
      endDate: endIso,
      dailyStartTime: this.generateForm.dailyStartTime,
      dailyEndTime: this.generateForm.dailyEndTime,
      slotDurationMinutes: this.generateForm.slotDurationMinutes,
      excludeWeekends: this.generateForm.excludeWeekends,
    };
    return { ...request, createdCountEstimate: estimateSlotCount(request) };
  }

  private validateGenerateForm(): { valid: boolean; errorKey?: string } {
    if (!this.generateForm.doctorId) {
      return { valid: false, errorKey: 'appointmentSlots.validation.doctorRequired' };
    }
    const startIso = parseDisplayDateToIso(this.generateForm.startDateDisplay, this.locale.currentLang);
    const endIso = parseDisplayDateToIso(this.generateForm.endDateDisplay, this.locale.currentLang);
    if (!startIso || !endIso) {
      return { valid: false, errorKey: 'appointmentSlots.validation.dateRangeRequired' };
    }
    if (endIso < startIso) {
      return { valid: false, errorKey: 'appointmentSlots.validation.endBeforeStart' };
    }
    if (!this.generateForm.dailyStartTime || !this.generateForm.dailyEndTime) {
      return { valid: false, errorKey: 'appointmentSlots.validation.timeRangeRequired' };
    }
    const [sh, sm] = this.generateForm.dailyStartTime.split(':').map(Number);
    const [eh, em] = this.generateForm.dailyEndTime.split(':').map(Number);
    if (eh * 60 + em <= sh * 60 + sm) {
      return { valid: false, errorKey: 'appointmentSlots.validation.endTimeBeforeStart' };
    }
    if (!this.slotDurationOptions.includes(this.generateForm.slotDurationMinutes)) {
      return { valid: false, errorKey: 'appointmentSlots.validation.durationRequired' };
    }
    return { valid: true };
  }

  previewSlots(): void {
    const validation = this.validateGenerateForm();
    if (!validation.valid) {
      this.formError = validation.errorKey ?? '';
      this.previewCount = 0;
      return;
    }
    const { createdCountEstimate, ...request } = this.buildGenerateRequest();
    this.previewCount = estimateSlotCount(request);
    this.formError = '';
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
