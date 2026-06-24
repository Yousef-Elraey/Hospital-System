import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DoctorService } from '../../services/doctor.service';
import type { CreateDoctorRequest } from '../../models/request/create-doctor-request.dto';
import type { DoctorResponse } from '../../models/response/doctor-response.dto';
import type { UpdateDoctorRequest } from '../../models/request/update-doctor-request.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-doctors',
  standalone: true,
  imports: [FormsModule, TranslateModule, PageHeaderComponent, IconComponent],
  templateUrl: './doctors.component.html',
  styleUrls: ['./doctors.component.css'],
})
export class DoctorsComponent implements OnInit {
  list: DoctorResponse[] = [];
  loading = false;
  showForm = false;
  editId: number | null = null;
  model: CreateDoctorRequest = { name: '', speciality: '', contactNumber: '' };
  filters = { name: '', speciality: '', contactNumber: '' };

  constructor(
    private doctorService: DoctorService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.doctorService
      .getDoctors({
        name: this.filters.name.trim() || undefined,
        speciality: this.filters.speciality.trim() || undefined,
        contactNumber: this.filters.contactNumber.trim() || undefined,
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
    this.filters = { name: '', speciality: '', contactNumber: '' };
    this.load();
  }

  openAdd(): void {
    this.editId = null;
    this.model = { name: '', speciality: '', contactNumber: '' };
    this.showForm = true;
  }

  openEdit(d: DoctorResponse): void {
    this.editId = d.id ?? null;
    this.model = { name: d.name, speciality: d.speciality, contactNumber: d.contactNumber };
    this.showForm = true;
  }

  cancel(): void {
    this.showForm = false;
    this.editId = null;
  }

  save(): void {
    if (!this.model.name?.trim()) return;
    const req = this.editId
      ? this.doctorService.updateDoctor(this.editId, this.model as UpdateDoctorRequest)
      : this.doctorService.addDoctor(this.model);
    req.subscribe({
      next: () => { this.showForm = false; this.editId = null; this.load(); },
    });
  }

  delete(d: DoctorResponse): void {
    if (!d.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteDoctor' })
      .subscribe((ok) => {
        if (!ok) return;
        this.doctorService.deleteDoctor(d.id!).subscribe({
          next: () => this.load(),
        });
      });
  }
}
