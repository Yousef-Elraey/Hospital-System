import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { DoctorService } from '../../services/doctor.service';
import { SpecialityService } from '../../../specialities/services/speciality.service';
import { LocaleService } from '../../../../core/services/locale.service';
import type { UpdateDoctorRequest } from '../../models/request/update-doctor-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-doctors-edit',
  standalone: true,
  imports: [FormsModule, TranslateModule, RouterLink, PageHeaderComponent, NgSelectModule, RequiredFormLabelComponent],
  templateUrl: './doctors-edit.component.html',
  styleUrls: ['./doctors-edit.component.css'],
})
export class DoctorsEditComponent implements OnInit {
  id: number | null = null;
  model: UpdateDoctorRequest = { name: '', specialityId: null, contactNumber: '' };
  specialitySelectOptions: { value: number; label: string }[] = [];
  loadingSpecialities = false;
  loading = false;

  constructor(
    private doctorService: DoctorService,
    private specialityService: SpecialityService,
    private locale: LocaleService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.loadSpecialities();
    if (this.id) this.load();
  }

  private loadSpecialities(): void {
    this.loadingSpecialities = true;
    this.specialityService.getSpecialities({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe({
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

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.doctorService.getDoctor(this.id).subscribe({
      next: (data) => {
        this.model = {
          name: data.name,
          specialityId: data.speciality?.id ?? null,
          contactNumber: data.contactNumber,
        };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.doctorService.updateDoctor(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/doctors', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/doctors', this.id]);
  }
}
