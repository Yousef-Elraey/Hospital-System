import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { MedicalRecordResponse } from '../../models/response/medical-record-response.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';

@Component({
  selector: 'app-medical-records-view',
  standalone: true,
  imports: [TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './medical-records-view.component.html',
  styleUrls: ['./medical-records-view.component.css'],
})
export class MedicalRecordsViewComponent implements OnInit {
  id: number | null = null;
  record: MedicalRecordResponse | null = null;
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  loading = false;

  constructor(private medicalRecordService: MedicalRecordService, private doctorService: DoctorService, private patientService: PatientService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.doctorService.getDoctors().subscribe((d) => (this.doctors = d ?? []));
    this.patientService.getPatients().subscribe((p) => (this.patients = p ?? []));
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.medicalRecordService.getMedicalRecord(this.id).subscribe({
      next: (data) => { this.record = data; this.loading = false; },
      error: () => { this.loading = false; },
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
