import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { BillingService } from '../../services/billing.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { UpdateBillingRequest } from '../../models/request/update-billing-request.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { RequiredFormLabelComponent } from '../../../../core/components/required-form-label/required-form-label.component';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-billing-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, RouterLink, PageHeaderComponent, NgSelectModule, RequiredFormLabelComponent],
  templateUrl: './billing-edit.component.html',
  styleUrls: ['./billing-edit.component.css'],
})
export class BillingEditComponent implements OnInit {
  id: number | null = null;
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  model: UpdateBillingRequest = { amount: 0, patient_id: 0 };
  loading = false;

  constructor(
    private billingService: BillingService, private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.patientService.getPatients({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.patients = response.data ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.billingService.getBilling(this.id).subscribe({
      next: (data) => {
        this.model = { amount: data.amount, patient_id: data.patient_id };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.billingService.updateBilling(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/billing', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/billing', this.id]);
  }

}
