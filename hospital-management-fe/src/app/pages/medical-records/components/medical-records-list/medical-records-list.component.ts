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
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { NgSelectModule } from '@ng-select/ng-select';
import { DEFAULT_PAGE_SIZE_OPTIONS, DROPDOWN_FETCH_SIZE, applyPageResponse, toPageRequest } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-medical-records-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, ListFilterToggleComponent, ListPaginationComponent, NgSelectModule],
  templateUrl: './medical-records-list.component.html',
  styleUrls: ['./medical-records-list.component.css'],
})
export class MedicalRecordsListComponent implements OnInit {
  list: MedicalRecordResponse[] = [];
  totalElements = 0;
  doctors: DoctorResponse[] = [];
  patients: PatientResponse[] = [];
  patientSelectOptions: { value: number; label: string }[] = [];
  doctorSelectOptions: { value: number; label: string }[] = [];
  loading = false;
  showFilters = false;
  filters = { patientId: null as number | null, doctorId: null as number | null, diagnose: '' };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

  constructor(
    private medicalRecordService: MedicalRecordService,
    private doctorService: DoctorService,
    private patientService: PatientService,
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
    this.medicalRecordService.getMedicalRecords({
      patientId: this.filters.patientId ?? undefined,
      doctorId: this.filters.doctorId ?? undefined,
      diagnose: this.filters.diagnose.trim() || undefined,
      ...toPageRequest(this.currentPage, this.pageSize),
    }).subscribe({
      next: (response) => {
        const page = applyPageResponse(response, { pageSize: this.pageSize });
        this.list = page.list;
        this.totalElements = page.totalElements;
        this.currentPage = page.currentPage;
        this.pageSize = page.pageSize;
        this.loading = false;
      },
      error: () => { this.loading = false; },
    });
  }

  setPage(page: number): void {
    this.currentPage = page;
    this.load();
  }

  setPageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
    this.load();
  }

  applyFilters(): void {
    this.currentPage = 1;
    this.load();
  }

  clearFilters(): void {
    this.filters = { patientId: null, doctorId: null, diagnose: '' };
    this.currentPage = 1;
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
