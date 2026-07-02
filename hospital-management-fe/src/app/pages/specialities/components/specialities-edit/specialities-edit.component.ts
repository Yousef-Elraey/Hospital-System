import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SpecialityService } from '../../services/speciality.service';
import type { UpdateSpecialityRequest } from '../../models/request/update-speciality-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';

@Component({
  selector: 'app-specialities-edit',
  standalone: true,
  imports: [FormsModule, TranslateModule, RouterLink, PageHeaderComponent, RequiredFormLabelComponent],
  templateUrl: './specialities-edit.component.html',
  styleUrls: ['./specialities-edit.component.css'],
})
export class SpecialitiesEditComponent implements OnInit {
  id: number | null = null;
  model: UpdateSpecialityRequest = { nameEn: '', nameAr: '' };
  loading = false;

  constructor(
    private specialityService: SpecialityService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.specialityService.getSpeciality(this.id).subscribe({
      next: (data) => {
        this.model = { nameEn: data.nameEn, nameAr: data.nameAr };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.specialityService.updateSpeciality(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/specialities', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/specialities', this.id]);
  }
}
