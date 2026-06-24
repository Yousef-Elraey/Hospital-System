import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { MedicalRecordResponse } from '../../models/response/medical-record-response.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { NgSelectModule } from '@ng-select/ng-select';

@Component({
  selector: 'app-medical-records-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, IconComponent, NgSelectModule],
  templateUrl: './medical-records-list.component.html',
  styleUrls: ['./medical-records-list.component.css'],
})
export class MedicalRecordsListComponent implements OnInit {
  list: MedicalRecordResponse[] = [];
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  filters = { patientId: null as number | null, doctorId: null as number | null, diagnose: '' };

  constructor(
    private medicalRecordService: MedicalRecordService, private doctorService: DoctorService, private patientService: PatientService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.load();
    this.doctorService.getDoctors().subscribe((d) => {
      this.doctors = d ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: x.name }));
    });
    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  load(): void {
    this.loading = true;
    this.medicalRecordService.getMedicalRecords({
      patientId: this.filters.patientId ?? undefined,
      doctorId: this.filters.doctorId ?? undefined,
      diagnose: this.filters.diagnose.trim() || undefined,
    }).subscribe({
      next: (data) => { this.list = data ?? []; this.loading = false; },
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
