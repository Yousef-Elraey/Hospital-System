import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { BillingService } from '../../services/billing.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { CreateBillingRequest } from '../../models/request/create-billing-request.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-billing-add',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, PageHeaderComponent, NgSelectModule],
  templateUrl: './billing-add.component.html',
  styleUrls: ['./billing-add.component.css'],
})
export class BillingAddComponent implements OnInit {
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  model: { amount?: number; patient_id: number | null } = { amount: undefined, patient_id: null };

  constructor(private billingService: BillingService, private patientService: PatientService, private router: Router) {}

  ngOnInit(): void {
    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  save(form: NgForm): void {
    if (form.invalid) return;
    const request: CreateBillingRequest = {
      amount: this.model.amount!,
      patient_id: this.model.patient_id!,
    };
    this.billingService.createBilling(request).subscribe({
      next: () => this.router.navigate(['/billing']),
    });
  }

  cancel(): void {
    this.router.navigate(['/billing']);
  }
}
