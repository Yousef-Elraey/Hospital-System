import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlContainer, FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import type { CreatePatientRequest } from '../../models/request/create-patient-request.dto';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';

@Component({
  selector: 'app-patient-form-fields',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, HospitalDatepickerComponent, NgSelectModule],
  viewProviders: [{ provide: ControlContainer, useExisting: NgForm }],
  templateUrl: './patient-form-fields.component.html',
})
export class PatientFormFieldsComponent {
  @Input({ required: true }) model!: CreatePatientRequest;
  @Input() dobDateStr = '';
  @Input() submitted = false;
  @Output() dobDateStrChange = new EventEmitter<string>();

  readonly genderSelectOptions = [
    { value: 'MALE', label: 'patients.gender.MALE' },
    { value: 'FEMALE', label: 'patients.gender.FEMALE' }
  ];

  onDobChange(value: string): void {
    this.dobDateStr = value;
    this.dobDateStrChange.emit(value);
  }
}
