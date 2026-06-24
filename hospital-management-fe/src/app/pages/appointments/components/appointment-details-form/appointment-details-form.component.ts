import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ControlContainer, FormsModule, NgForm } from '@angular/forms';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { AppointmentTimePickerComponent } from '../appointment-time-picker/appointment-time-picker.component';

@Component({
  selector: 'app-appointment-details-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    HospitalDatepickerComponent,
    AppointmentTimePickerComponent,
    NgSelectModule,
  ],
  viewProviders: [{ provide: ControlContainer, useExisting: NgForm }],
  templateUrl: './appointment-details-form.component.html',
})
export class AppointmentDetailsFormComponent {
  @Input({ required: true }) doctorSelectOptions!: { value: number; label: string }[];
  readonly appointmentTypeOptions: { value: 'INITIAL_CONSULTATION' | 'FOLLOW_UP_CONSULTATION'; labelKey: string }[] = [
    { value: 'INITIAL_CONSULTATION', labelKey: 'appointments.appointmentType.INITIAL_CONSULTATION' },
    { value: 'FOLLOW_UP_CONSULTATION', labelKey: 'appointments.appointmentType.FOLLOW_UP_CONSULTATION' },
  ];

  @Input() minDate?: NgbDate;
  @Input() maxDate?: NgbDate;

  @Input() doctorId: number | null | undefined = null;
  @Output() doctorIdChange = new EventEmitter<number | null | undefined>();

  @Input() dateStr = '';
  @Output() dateStrChange = new EventEmitter<string>();

  @Input() time = '';
  @Output() timeChange = new EventEmitter<string>();

  @Input() appointmentType: 'INITIAL_CONSULTATION' | 'FOLLOW_UP_CONSULTATION' | null | undefined = 'INITIAL_CONSULTATION';
  @Output() appointmentTypeChange = new EventEmitter<'INITIAL_CONSULTATION' | 'FOLLOW_UP_CONSULTATION' | null | undefined>();

  /** ngModel control names to avoid collisions between pages */
  @Input() doctorName = 'doctorId';
  @Input() dateName = 'apptDate';
  @Input() timeName = 'apptTime';
  @Input() appointmentTypeName = 'appointmentType';

  /** ids for accessibility */
  @Input() doctorInputId = 'apptDoctor';
  @Input() timeInputId = 'apptTime';
  @Input() appointmentTypeInputId = 'apptType';

  /** Parent form submission state (template-driven forms). */
  @Input() submitted = false;
}

