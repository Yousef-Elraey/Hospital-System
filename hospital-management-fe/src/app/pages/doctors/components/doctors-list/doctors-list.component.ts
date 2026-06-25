import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DoctorService } from '../../services/doctor.service';
import type { DoctorResponse } from '../../models/response/doctor-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-doctors-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, IconComponent],
  templateUrl: './doctors-list.component.html',
  styleUrls: ['./doctors-list.component.css'],
})
export class DoctorsListComponent implements OnInit {
  list: DoctorResponse[] = [];
  loading = false;
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
    this.doctorService.getDoctors({
      name: this.filters.name.trim() || undefined,
      speciality: this.filters.speciality.trim() || undefined,
      contactNumber: this.filters.contactNumber.trim() || undefined,
    }).subscribe({
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
