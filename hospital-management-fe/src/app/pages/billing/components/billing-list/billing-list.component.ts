import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { BillingService } from '../../services/billing.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { BillingResponse } from '../../models/response/billing-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { NgSelectModule } from '@ng-select/ng-select';

@Component({
  selector: 'app-billing-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, IconComponent, NgSelectModule],
  templateUrl: './billing-list.component.html',
  styleUrls: ['./billing-list.component.css'],
})
export class BillingListComponent implements OnInit {
  list: BillingResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  filters = { patient_id: null as number | null, amount: null as number | null };

  constructor(
    private billingService: BillingService, private patientService: PatientService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.load();
    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  load(): void {
    this.loading = true;
    this.billingService.getBillings({
      patient_id: this.filters.patient_id ?? undefined,
      amount: this.filters.amount ?? undefined,
    }).subscribe({
      next: (data) => { this.list = data ?? []; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  applyFilters(): void {
    this.load();
  }

  clearFilters(): void {
    this.filters = { patient_id: null, amount: null };
    this.load();
  }

  delete(b: BillingResponse): void {
    if (!b.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteBilling' })
      .subscribe((ok) => {
        if (!ok) return;
        this.billingService.deleteBilling(b.id!).subscribe({
          next: () => this.load(),
        });
      });
  }

  patientName(patientId: number): string {
    const p = this.patients.find((x) => x.id === patientId);
    return p ? p.name : String(patientId);
  }
}
