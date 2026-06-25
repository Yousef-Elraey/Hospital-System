import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { DoctorService } from '../../services/doctor.service';
import { SpecialityService } from '../../../specialities/services/speciality.service';
import { LocaleService } from '../../../../core/services/locale.service';
import type { CreateDoctorRequest } from '../../models/request/create-doctor-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-doctors-add',
  standalone: true,
  imports: [FormsModule, TranslateModule, PageHeaderComponent, NgSelectModule],
  templateUrl: './doctors-add.component.html',
  styleUrls: ['./doctors-add.component.css'],
})
export class DoctorsAddComponent implements OnInit {
  model: CreateDoctorRequest = { name: '', specialityId: null, contactNumber: '' };
  specialitySelectOptions: { value: number; label: string }[] = [];
  loadingSpecialities = false;

  constructor(
    private doctorService: DoctorService,
    private specialityService: SpecialityService,
    private locale: LocaleService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadSpecialities();
  }

  private loadSpecialities(): void {
    this.loadingSpecialities = true;
    this.specialityService.getSpecialities({ page: 0, size: 100 }).subscribe({
      next: (response) => {
        const isAr = this.locale.currentLang === 'ar';
        this.specialitySelectOptions = (response.data ?? []).map((s) => ({
          value: s.id,
          label: isAr ? s.nameAr : s.nameEn,
        }));
        this.loadingSpecialities = false;
      },
      error: () => { this.loadingSpecialities = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid) return;
    this.doctorService.addDoctor(this.model).subscribe({
      next: () => this.router.navigate(['/doctors']),
    });
  }

  cancel(): void {
    this.router.navigate(['/doctors']);
  }
}
