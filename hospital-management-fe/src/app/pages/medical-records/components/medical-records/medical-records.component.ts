import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { CreateMedicalRecordRequest } from '../../models/request/create-medical-record-request.dto';
import type { MedicalRecordResponse } from '../../models/response/medical-record-response.dto';
import type { UpdateMedicalRecordRequest } from '../../models/request/update-medical-record-request.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-medical-records',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, PageHeaderComponent, IconComponent, NgSelectModule],
  templateUrl: './medical-records.component.html',
  styleUrls: ['./medical-records.component.css'],
})
export class MedicalRecordsComponent implements OnInit {
  list: MedicalRecordResponse[] = [];
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  showForm = false;
  editId: number | null = null;
  model: CreateMedicalRecordRequest = { diagnose: '', treatment: '', patientId: 0, doctorId: 0 };
  filters = { patientId: null as number | null, doctorId: null as number | null, diagnose: '' };

  constructor(
    private medicalRecordService: MedicalRecordService, private doctorService: DoctorService, private patientService: PatientService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.load();
    this.doctorService.getDoctors({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.doctors = response.data ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: x.name }));
    });
    this.patientService.getPatients({ page: 0, size: DROPDOWN_FETCH_SIZE }).subscribe((response) => {
      this.patients = response.data ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  load(): void {
    this.loading = true;
    this.medicalRecordService
      .getMedicalRecords({
        patientId: this.filters.patientId ?? undefined,
        doctorId: this.filters.doctorId ?? undefined,
        diagnose: this.filters.diagnose.trim() || undefined,
      })
      .subscribe({
      next: (response) => { this.list = response.data ?? []; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  applyFilters(): void {
    this.load();
  }

  clearFilters(): void {
    this.filters = { patientId: null, doctorId: null, diagnose: '' };
    this.load();
  }

  openAdd(): void {
    this.editId = null;
    this.model = { diagnose: '', treatment: '', patientId: 0, doctorId: 0 };
    this.showForm = true;
  }

  openEdit(m: MedicalRecordResponse): void {
    this.editId = m.id ?? null;
    this.model = {
      diagnose: m.diagnose,
      treatment: m.treatment,
      patientId: m.patientId,
      doctorId: m.doctorId,
    };
    this.showForm = true;
  }

  cancel(): void {
    this.showForm = false;
    this.editId = null;
  }

  save(): void {
    if (!this.model.diagnose?.trim() || !this.model.patientId || !this.model.doctorId) return;
    const req = this.editId
      ? this.medicalRecordService.updateMedicalRecord(this.editId, this.model as UpdateMedicalRecordRequest)
      : this.medicalRecordService.addMedicalRecord(this.model);
    req.subscribe({
      next: () => { this.showForm = false; this.editId = null; this.load(); },
    });
  }

  delete(m: MedicalRecordResponse): void {
    if (!m.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteRecord' })
      .subscribe((ok) => {
        if (!ok) return;
        this.medicalRecordService.deleteMedicalRecord(m.id!).subscribe({
          next: () => this.load(),
        });
      });
  }

  patientName(id: number): string {
    const p = this.patients.find((x) => x.id === id);
    return p ? p.name : String(id);
  }

  doctorName(id: number): string {
    const d = this.doctors.find((x) => x.id === id);
    return d ? d.name : String(id);
  }

}
