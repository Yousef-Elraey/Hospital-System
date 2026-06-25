import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { PatientService } from '../../services/patient.service';
import type { PatientResponse } from '../../models/response/patient-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { IconComponent } from '../../../../core/components/icon/icon.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { LocaleService } from '../../../../core/services/locale.service';
import { formatDateOfBirth } from '../../utils/format-date-of-birth';
import { parseDisplayDateToIso } from '../../../../core/utils/display-date';

@Component({
  selector: 'app-patients-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, IconComponent],
  templateUrl: './patients-list.component.html',
  styleUrls: ['./patients-list.component.css'],
})
export class PatientsListComponent implements OnInit {
  list: PatientResponse[] = [];
  loading = false;
  filters = { name: '', dateOfBirth: '', phone: '' };

  constructor(
    private patientService: PatientService,
    private confirmDialog: ConfirmDialogService,
    private locale: LocaleService,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    const dateOfBirthIso = parseDisplayDateToIso(this.filters.dateOfBirth, this.locale.currentLang) ?? undefined;
    this.patientService.getPatients({
      name: this.filters.name.trim() || undefined,
      dateOfBirth: dateOfBirthIso,
      phone: this.filters.phone.trim() || undefined,
      mobile: this.filters.phone.trim() || undefined,
    }).subscribe({
      next: (data) => { this.list = data ?? []; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  applyFilters(): void {
    this.load();
  }

  clearFilters(): void {
    this.filters = { name: '', dateOfBirth: '', phone: '' };
    this.load();
  }

  formatDob(value: string | undefined): string {
    return formatDateOfBirth(value, this.locale.currentLang);
  }

  delete(p: PatientResponse): void {
    if (!p.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deletePatient' })
      .subscribe((ok) => {
        if (!ok) return;
        this.patientService.deletePatient(p.id!).subscribe({
          next: () => this.load(),
        });
      });
  }
}
