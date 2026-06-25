import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { PatientService } from '../../services/patient.service';
import type { UpdatePatientRequest } from '../../models/request/update-patient-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { Gender, GENDER_OPTIONS } from '../../models/enums/gender.enum';
import { LocaleService } from '../../../../core/services/locale.service';
import { HospitalDatepickerComponent } from '../../../common/components/hospital-datepicker/hospital-datepicker.component';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';

@Component({
  selector: 'app-patients-edit',
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
  templateUrl: './patients-edit.component.html',
  styleUrls: ['./patients-edit.component.css'],
})
export class PatientsEditComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  genderSelectOptions: { value: Gender; label: string }[] = [];
  id: number | null = null;
  model: UpdatePatientRequest = { name: '', gender: '', phone: '', dateOfBirth: '' };
  dobDateStr = '';
  loading = false;

  constructor(
    private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute,
    public locale: LocaleService,
    private translate: TranslateService,
  ) {
    this.rebuildGenderOptions();
    this.translate.onLangChange.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => this.rebuildGenderOptions());
  }

  private rebuildGenderOptions(): void {
    this.genderSelectOptions = GENDER_OPTIONS.map((g) => ({
      value: g,
      label: this.translate.instant('patients.gender.' + g),
    }));
  }

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.patientService.getPatient(this.id).subscribe({
      next: (data) => {
        this.model = {
          name: data.name,
          gender: data.gender,
          phone: data.phone,
          dateOfBirth: data.dateOfBirth,
        };
        const dob = this.model.dateOfBirth;
        if (dob && dob.length > 10) {
          this.model.dateOfBirth = dob.slice(0, 10);
        }
        this.dobDateStr = (this.model.dateOfBirth || '').slice(0, 10);
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    const iso = parseDisplayDateToIso(this.dobDateStr, this.locale.currentLang);
    if (!iso) return;
    this.model.dateOfBirth = iso;
    this.patientService.updatePatient(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/patients', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/patients', this.id]);
  }
}
