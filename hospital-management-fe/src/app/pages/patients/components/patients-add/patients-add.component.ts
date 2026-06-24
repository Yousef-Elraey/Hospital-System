import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { PatientService } from '../../services/patient.service';
import type { CreatePatientRequest } from '../../models/request/create-patient-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { LocaleService } from '../../../../core/services/locale.service';
import { PatientFormFieldsComponent } from '../patient-form-fields/patient-form-fields.component';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';

@Component({
  selector: 'app-patients-add',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    PageHeaderComponent,
    PatientFormFieldsComponent,
  ],
  templateUrl: './patients-add.component.html',
  styleUrls: ['./patients-add.component.css'],
})
export class PatientsAddComponent {
  model: CreatePatientRequest = { name: '', gender: '', phone: '', dateOfBirth: '' };
  /** Bound to hospital datepicker (locale display; API stores YYYY-MM-DD). */
  dobDateStr = '';

  constructor(
    private patientService: PatientService,
    private router: Router,
    public locale: LocaleService,
  ) {}

  save(form: NgForm): void {
    if (form.invalid) return;
    const iso = parseDisplayDateToIso(this.dobDateStr, this.locale.currentLang);
    if (!iso) return;
    this.model.dateOfBirth = iso;
    this.patientService.addPatient(this.model).subscribe({
      next: () => this.router.navigate(['/patients']),
    });
  }

  cancel(): void {
    this.router.navigate(['/patients']);
  }
}
