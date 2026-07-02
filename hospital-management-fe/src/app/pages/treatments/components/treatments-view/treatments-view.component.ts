import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TreatmentService } from '../../services/treatment.service';
import type { TreatmentResponse } from '../../models/response/treatment-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-treatments-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './treatments-view.component.html',
  styleUrls: ['./treatments-view.component.css'],
})
export class TreatmentsViewComponent implements OnInit {
  id: number | null = null;
  treatment: TreatmentResponse | null = null;
  loading = false;

  constructor(private treatmentService: TreatmentService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.treatmentService.getTreatment(this.id).subscribe({
      next: (data) => { this.treatment = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }
}
