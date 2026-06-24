import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { PatientService } from '../../services/patient.service';
import type { PatientResponse } from '../../models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { LocaleService } from '../../../../core/services/locale.service';
import { formatDateOfBirth } from '../../utils/format-date-of-birth';

@Component({
  selector: 'app-patients-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './patients-view.component.html',
  styleUrls: ['./patients-view.component.css'],
})
export class PatientsViewComponent implements OnInit {
  id: number | null = null;
  patient: PatientResponse | null = null;
  loading = false;

  constructor(
    private patientService: PatientService,
    private route: ActivatedRoute,
    private locale: LocaleService,
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.patientService.getPatient(this.id).subscribe({
      next: (data) => { this.patient = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  formatDob(value: string | undefined): string {
    return formatDateOfBirth(value, this.locale.currentLang);
  }
}
