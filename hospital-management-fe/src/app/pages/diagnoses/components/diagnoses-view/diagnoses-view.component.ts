import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { DiagnoseService } from '../../services/diagnose.service';
import type { DiagnoseResponse } from '../../models/response/diagnose-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-diagnoses-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './diagnoses-view.component.html',
  styleUrls: ['./diagnoses-view.component.css'],
})
export class DiagnosesViewComponent implements OnInit {
  id: number | null = null;
  diagnose: DiagnoseResponse | null = null;
  loading = false;

  constructor(private diagnoseService: DiagnoseService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.diagnoseService.getDiagnose(this.id).subscribe({
      next: (data) => { this.diagnose = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }
}
