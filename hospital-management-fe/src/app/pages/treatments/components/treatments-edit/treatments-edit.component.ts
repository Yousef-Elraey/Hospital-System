import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TreatmentService } from '../../services/treatment.service';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';

@Component({
  selector: 'app-treatments-edit',
  standalone: true,
  imports: [FormsModule, TranslateModule, RouterLink, PageHeaderComponent, RequiredFormLabelComponent],
  templateUrl: './treatments-edit.component.html',
  styleUrls: ['./treatments-edit.component.css'],
})
export class TreatmentsEditComponent implements OnInit {
  id: number | null = null;
  model = { nameEn: '', nameAr: '', activeIngredient: '' };
  loading = false;

  constructor(
    private treatmentService: TreatmentService,
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
    this.treatmentService.getTreatment(this.id).subscribe({
      next: (data) => {
        this.model = { nameEn: data.nameEn, nameAr: data.nameAr, activeIngredient: data.activeIngredient };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.treatmentService.updateTreatment({ id: this.id, ...this.model }).subscribe({
      next: () => this.router.navigate(['/treatments', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/treatments', this.id]);
  }
}
