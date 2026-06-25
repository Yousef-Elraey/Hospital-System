import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MedicalRecordService } from '../../services/medical-record.service';
import { DoctorService } from '../../../doctors/services/doctor.service';
import { PatientService } from '../../../patients/services/patient.service';
import type { MedicalRecordResponse } from '../../models/response/medical-record-response.dto';
import type { DoctorResponse } from '../../../doctors/models/response/doctor-response.dto';
import type { PatientResponse } from '../../../patients/models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { LocaleService } from '../../../../core/services/locale.service';
import { formatDateDisplay } from '../../../../core/utils/display-date';
import { formatDateTimeDisplay } from '../../../appointments/utils/date-form';
import {
  mainDiagnosis,
  parseDiagnoses,
  parseTreatment,
  visitNumber,
  type ParsedMedication,
  type ParsedTreatment,
} from '../../utils/medical-record-display';

@Component({
  selector: 'app-medical-records-view',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, RouterLink, PageHeaderComponent],
  templateUrl: './medical-records-view.component.html',
  styleUrls: ['./medical-records-view.component.css'],
})
export class MedicalRecordsViewComponent implements OnInit {
  id: number | null = null;
  patient: PatientResponse | null = null;
  selectedRecord: MedicalRecordResponse | null = null;
  visits: MedicalRecordResponse[] = [];
  doctors: DoctorResponse[] = [];
  loading = false;

  searchQuery = '';
  showFilters = false;
  filterDoctorId: number | null = null;
  filterVisitType = '';

  readonly pageSizeOptions = [5, 10, 20];
  pageSize = 5;
  currentPage = 1;

  constructor(
    private medicalRecordService: MedicalRecordService,
    private doctorService: DoctorService,
    private patientService: PatientService,
    private route: ActivatedRoute,
    private router: Router,
    public locale: LocaleService,
  ) {}

  ngOnInit(): void {
    const idStr = this.route.snapshot.paramMap.get('id');
    this.id = idStr ? +idStr : null;
    this.doctorService.getDoctors().subscribe((d) => (this.doctors = d ?? []));
    if (this.id) this.load();
  }

  load(): void {
    if (!this.id) return;
    this.loading = true;
    this.medicalRecordService.getMedicalRecord(this.id).subscribe({
      next: (record) => {
        this.selectedRecord = record;
        this.patientService.getPatient(record.patientId).subscribe({
          next: (p) => {
            this.patient = p;
            this.loadVisits(record.patientId);
          },
          error: () => {
            this.loading = false;
          },
        });
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  private loadVisits(patientId: number): void {
    this.medicalRecordService.getMedicalRecordsByPatientId(patientId).subscribe({
      next: (data) => {
        this.visits = (data ?? []).sort((a, b) => this.recordTime(b) - this.recordTime(a));
        this.syncSelectedFromRoute();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  private syncSelectedFromRoute(): void {
    if (!this.id) return;
    const match = this.visits.find((v) => v.id === this.id);
    if (match) this.selectedRecord = match;
  }

  private recordTime(r: MedicalRecordResponse): number {
    const t = r.createdAt ?? r.updatedAt;
    return t ? new Date(t).getTime() : r.id;
  }

  get filteredVisits(): MedicalRecordResponse[] {
    const q = this.searchQuery.trim().toLowerCase();
    return this.visits.filter((v) => {
      if (this.filterDoctorId && v.doctorId !== this.filterDoctorId) return false;
      const vt = this.resolveVisitType(v);
      if (this.filterVisitType && vt !== this.filterVisitType) return false;
      if (!q) return true;
      const haystack = [
        visitNumber(v.id, v.createdAt),
        this.formatVisitDate(v),
        this.doctorName(v.doctorId),
        mainDiagnosis(v.diagnose),
        v.diagnose,
        v.treatment,
      ]
        .join(' ')
        .toLowerCase();
      return haystack.includes(q);
    });
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredVisits.length / this.pageSize));
  }

  get pagedVisits(): MedicalRecordResponse[] {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredVisits.slice(start, start + this.pageSize);
  }

  get pageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  patientAge(): number | null {
    const dob = this.patient?.dateOfBirth;
    if (!dob) return null;
    const iso = dob.slice(0, 10);
    if (!/^\d{4}-\d{2}-\d{2}$/.test(iso)) return null;
    const [y, m, d] = iso.split('-').map(Number);
    const today = new Date();
    let age = today.getFullYear() - y;
    const monthDiff = today.getMonth() + 1 - m;
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < d)) age--;
    return age >= 0 ? age : null;
  }

  formatDob(): string {
    return formatDateDisplay(this.patient?.dateOfBirth, this.locale.currentLang);
  }

  genderKey(): string {
    const g = this.patient?.gender;
    return g ? `patients.gender.${g.toUpperCase()}` : '';
  }

  doctorName(id: number): string {
    const d = this.doctors.find((x) => x.id === id);
    return d ? d.name : String(id);
  }

  formatVisitDate(record: MedicalRecordResponse): string {
    const t = record.createdAt ?? record.updatedAt;
    return t ? formatDateTimeDisplay(t, this.locale.currentLang) : '-';
  }

  formatVisitDateShort(record: MedicalRecordResponse): string {
    const t = record.createdAt ?? record.updatedAt;
    return t ? formatDateDisplay(t.slice(0, 10), this.locale.currentLang) : '-';
  }

  visitNo(record: MedicalRecordResponse): string {
    return visitNumber(record.id, record.createdAt);
  }

  mainDx(record: MedicalRecordResponse): string {
    return mainDiagnosis(record.diagnose);
  }

  resolveVisitType(record: MedicalRecordResponse): string {
    return parseTreatment(record.treatment).visitType ?? 'outpatient';
  }

  visitTypeLabelKey(record: MedicalRecordResponse): string {
    const map: Record<string, string> = {
      outpatient: 'medicalRecords.visitTypeOutpatient',
      inpatient: 'medicalRecords.visitTypeInpatient',
      emergency: 'medicalRecords.visitTypeEmergency',
    };
    return map[this.resolveVisitType(record)] ?? map['outpatient'];
  }

  selectedDiagnoses(): string[] {
    return parseDiagnoses(this.selectedRecord?.diagnose);
  }

  selectedTreatment(): ParsedTreatment {
    return parseTreatment(this.selectedRecord?.treatment);
  }

  medicationDisplayName(med: ParsedMedication): string {
    return [med.name, med.dose].filter(Boolean).join(' ');
  }

  medicationInstruction(med: ParsedMedication): string {
    const parts: string[] = [];
    if (med.frequency) parts.push(med.frequency);
    if (med.duration) parts.push(med.duration);
    return parts.join(' - ');
  }

  isSelected(record: MedicalRecordResponse): boolean {
    return this.selectedRecord?.id === record.id;
  }

  selectVisit(record: MedicalRecordResponse): void {
    this.selectedRecord = record;
    this.id = record.id;
    this.router.navigate(['/medical-records', record.id], { replaceUrl: true });
  }

  onSearchChange(): void {
    this.currentPage = 1;
  }

  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }

  clearFilters(): void {
    this.filterDoctorId = null;
    this.filterVisitType = '';
    this.searchQuery = '';
    this.currentPage = 1;
  }

  setPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
  }

  setPageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
  }

  exportVisits(): void {
    const rows = [
      ['Visit', 'Date', 'Doctor', 'Visit Type', 'Diagnosis'].join(','),
      ...this.filteredVisits.map((v) =>
        [
          this.visitNo(v),
          this.formatVisitDate(v),
          this.doctorName(v.doctorId),
          this.resolveVisitType(v),
          `"${mainDiagnosis(v.diagnose).replace(/"/g, '""')}"`,
        ].join(','),
      ),
    ];
    const blob = new Blob([rows.join('\n')], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `patient-visits-${this.patient?.id ?? 'export'}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  printPage(): void {
    window.print();
  }
}
