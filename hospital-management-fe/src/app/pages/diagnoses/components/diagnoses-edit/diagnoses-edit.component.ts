import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DiagnoseService } from '../../services/diagnose.service';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';

@Component({
  selector: 'app-diagnoses-edit',
  standalone: true,
  imports: [FormsModule, TranslateModule, RouterLink, PageHeaderComponent, RequiredFormLabelComponent],
  templateUrl: './diagnoses-edit.component.html',
  styleUrls: ['./diagnoses-edit.component.css'],
})
export class DiagnosesEditComponent implements OnInit {
  id: number | null = null;
  model = { nameEn: '', nameAr: '' };
  loading = false;

  constructor(
    private diagnoseService: DiagnoseService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.diagnoseService.getDiagnose(this.id).subscribe({
      next: (data) => {
        this.model = { nameEn: data.nameEn, nameAr: data.nameAr };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.diagnoseService.updateDiagnose({ id: this.id, ...this.model }).subscribe({
      next: () => this.router.navigate(['/diagnoses', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/diagnoses', this.id]);
  }
}
