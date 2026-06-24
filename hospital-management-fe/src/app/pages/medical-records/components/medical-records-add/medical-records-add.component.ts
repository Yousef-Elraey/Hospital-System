import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { CreateMedicalRecordRequest } from '../../models/request/create-medical-record-request.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-medical-records-add',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, PageHeaderComponent, NgSelectModule],
  templateUrl: './medical-records-add.component.html',
  styleUrls: ['./medical-records-add.component.css'],
})
export class MedicalRecordsAddComponent implements OnInit {
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  model: { diagnose: string; treatment: string; patientId: number | null; doctorId: number | null } = {
    diagnose: '',
    treatment: '',
    patientId: null,
    doctorId: null,
  };

  constructor(private medicalRecordService: MedicalRecordService, private doctorService: DoctorService, private patientService: PatientService, private router: Router) {}

  ngOnInit(): void {
    this.doctorService.getDoctors().subscribe((d) => {
      this.doctors = d ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: x.name }));
    });
    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
  }

  save(form: NgForm): void {
    if (form.invalid) return;
    const request: CreateMedicalRecordRequest = {
      diagnose: this.model.diagnose,
      treatment: this.model.treatment,
      patientId: this.model.patientId!,
      doctorId: this.model.doctorId!,
    };
    this.medicalRecordService.addMedicalRecord(request).subscribe({
      next: () => this.router.navigate(['/medical-records']),
    });
  }

  cancel(): void {
    this.router.navigate(['/medical-records']);
  }
}
