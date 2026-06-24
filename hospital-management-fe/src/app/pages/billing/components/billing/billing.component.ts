import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { BillingService } from '../../services/billing.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { BillingResponse } from '../../models/response/billing-response.dto';
import type { CreateBillingRequest } from '../../models/request/create-billing-request.dto';
import type { UpdateBillingRequest } from '../../models/request/update-billing-request.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-billing',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, PageHeaderComponent, IconComponent, NgSelectModule],
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.css'],
})
export class BillingComponent implements OnInit {
  list: BillingResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  showForm = false;
  editId: number | null = null;
  model: CreateBillingRequest = { amount: 0, patient_id: 0 };
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
    this.billingService
      .getBillings({
        patient_id: this.filters.patient_id ?? undefined,
        amount: this.filters.amount ?? undefined,
      })
      .subscribe({
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

  openAdd(): void {
    this.editId = null;
    this.model = { amount: 0, patient_id: 0 };
    this.showForm = true;
  }

  openEdit(b: BillingResponse): void {
    this.editId = b.id ?? null;
    this.model = { amount: b.amount, patient_id: b.patient_id };
    this.showForm = true;
  }

  cancel(): void {
    this.showForm = false;
    this.editId = null;
  }

  save(): void {
    if (this.model.patient_id == null) return;
    const req = this.editId
      ? this.billingService.updateBilling(this.editId, this.model as UpdateBillingRequest)
      : this.billingService.createBilling(this.model);
    req.subscribe({
      next: () => { this.showForm = false; this.editId = null; this.load(); },
    });
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
