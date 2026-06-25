import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { BillingService } from '../../services/billing.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { BillingResponse } from '../../models/response/billing-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-billing-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './billing-view.component.html',
  styleUrls: ['./billing-view.component.css'],
})
export class BillingViewComponent implements OnInit {
  id: number | null = null;
  billing: BillingResponse | null = null;
  patients: PatientResponse[] = [];
  loading = false;

  constructor(private billingService: BillingService, private patientService: PatientService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.patientService.getPatients().subscribe((p) => (this.patients = p ?? []));
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.billingService.getBilling(this.id).subscribe({
      next: (data) => { this.billing = data; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  patientName(patientId: number): string {
    const p = this.patients.find((x) => x.id === patientId);
    return p ? p.name : String(patientId);
  }
}
