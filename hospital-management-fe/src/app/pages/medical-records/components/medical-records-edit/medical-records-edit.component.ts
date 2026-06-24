import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { UpdateMedicalRecordRequest } from '../../models/request/update-medical-record-request.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-medical-records-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, RouterLink, PageHeaderComponent, NgSelectModule],
  templateUrl: './medical-records-edit.component.html',
  styleUrls: ['./medical-records-edit.component.css'],
})
export class MedicalRecordsEditComponent implements OnInit {
  id: number | null = null;
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  model: UpdateMedicalRecordRequest = { diagnose: '', treatment: '', patientId: 0, doctorId: 0 };
  loading = false;

  constructor(
    private medicalRecordService: MedicalRecordService, private doctorService: DoctorService, private patientService: PatientService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.doctorService.getDoctors().subscribe((d) => {
      this.doctors = d ?? [];
      this.doctorSelectOptions = this.doctors.map((x) => ({ value: x.id!, label: x.name }));
    });
    this.patientService.getPatients().subscribe((p) => {
      this.patients = p ?? [];
      this.patientSelectOptions = this.patients.map((x) => ({ value: x.id!, label: x.name }));
    });
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.medicalRecordService.getMedicalRecord(this.id).subscribe({
      next: (data) => {
        this.model = {
          diagnose: data.diagnose,
          treatment: data.treatment,
          patientId: data.patientId,
          doctorId: data.doctorId,
        };
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  save(form: NgForm): void {
    if (form.invalid || !this.id) return;
    this.medicalRecordService.updateMedicalRecord(this.id, this.model).subscribe({
      next: () => this.router.navigate(['/medical-records', this.id]),
    });
  }

  cancel(): void {
    this.router.navigate(['/medical-records', this.id]);
  }

}
