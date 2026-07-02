import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TreatmentService } from '../../services/treatment.service';
import type { CreateTreatmentRequest } from '../../models/request/create-treatment-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';

@Component({
  selector: 'app-treatments-add',
  standalone: true,
  imports: [FormsModule, TranslateModule, PageHeaderComponent, RequiredFormLabelComponent],
  templateUrl: './treatments-add.component.html',
  styleUrls: ['./treatments-add.component.css'],
})
export class TreatmentsAddComponent {
  model: CreateTreatmentRequest = { nameEn: '', nameAr: '', activeIngredient: '' };

  constructor(private treatmentService: TreatmentService, private router: Router) {}

  save(form: NgForm): void {
    if (form.invalid) return;
    this.treatmentService.addTreatment(this.model).subscribe({
      next: () => this.router.navigate(['/treatments']),
    });
  }

  cancel(): void {
    this.router.navigate(['/treatments']);
  }
}
