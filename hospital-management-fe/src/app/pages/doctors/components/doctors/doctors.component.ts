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
import { LocaleService } from '../../../../core/services/locale.service';
import { DROPDOWN_FETCH_SIZE } from '../../../../core/utils/list-pagination';
import { specialityDisplayName } from '../../utils/speciality-display-name';

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
  model: CreateDoctorRequest = { name: '', specialityId: null, contactNumber: '' };
  filters = { name: '', speciality: '', contactNumber: '' };

  constructor(
    private doctorService: DoctorService,
    private confirmDialog: ConfirmDialogService,
    public locale: LocaleService,
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
        page: 0,
        size: DROPDOWN_FETCH_SIZE,
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
    this.filters = { name: '', speciality: '', contactNumber: '' };
    this.load();
  }

  openAdd(): void {
    this.editId = null;
    this.model = { name: '', specialityId: null, contactNumber: '' };
    this.showForm = true;
  }

  openEdit(d: DoctorResponse): void {
    this.editId = d.id ?? null;
    this.model = { name: d.name, specialityId: d.speciality?.id ?? null, contactNumber: d.contactNumber };
    this.showForm = true;
  }

  specialityName(doctor: DoctorResponse): string {
    return specialityDisplayName(doctor.speciality, this.locale.currentLang);
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
