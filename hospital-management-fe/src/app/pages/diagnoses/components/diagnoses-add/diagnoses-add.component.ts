import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DiagnoseService } from '../../services/diagnose.service';
import type { CreateDiagnoseRequest } from '../../models/request/create-diagnose-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';

@Component({
  selector: 'app-diagnoses-add',
  standalone: true,
  imports: [FormsModule, TranslateModule, PageHeaderComponent, RequiredFormLabelComponent],
  templateUrl: './diagnoses-add.component.html',
  styleUrls: ['./diagnoses-add.component.css'],
})
export class DiagnosesAddComponent {
  model: CreateDiagnoseRequest = { nameEn: '', nameAr: '' };

  constructor(private diagnoseService: DiagnoseService, private router: Router) {}

  save(form: NgForm): void {
    if (form.invalid) return;
    this.diagnoseService.addDiagnose(this.model).subscribe({
      next: () => this.router.navigate(['/diagnoses']),
    });
  }

  cancel(): void {
    this.router.navigate(['/diagnoses']);
  }
}
