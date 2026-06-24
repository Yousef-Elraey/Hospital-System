import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SpecialityService } from '../../services/speciality.service';
import type { CreateSpecialityRequest } from '../../models/request/create-speciality-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-specialities-add',
  standalone: true,
  imports: [FormsModule, TranslateModule, PageHeaderComponent],
  templateUrl: './specialities-add.component.html',
  styleUrls: ['./specialities-add.component.css'],
})
export class SpecialitiesAddComponent {
  model: CreateSpecialityRequest = { nameEn: '', nameAr: '' };
  constructor(private specialityService: SpecialityService, private router: Router) {}

  save(form: NgForm): void {
    if (form.invalid) return;
    this.specialityService.addSpeciality(this.model).subscribe({
      next: () => this.router.navigate(['/specialities']),
    });
  }

  cancel(): void {
    this.router.navigate(['/specialities']);
  }
}
