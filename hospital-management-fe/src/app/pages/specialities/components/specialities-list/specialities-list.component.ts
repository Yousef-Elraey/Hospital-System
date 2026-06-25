import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SpecialityService } from '../../services/speciality.service';
import type { SpecialityResponse } from '../../models/response/speciality-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';

@Component({
  selector: 'app-specialities-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent],
  templateUrl: './specialities-list.component.html',
  styleUrls: ['./specialities-list.component.css'],
})
export class SpecialitiesListComponent implements OnInit {
  list: SpecialityResponse[] = [];
  loading = false;
  filters = { nameEn: '', nameAr: '' };

  constructor(
    private specialityService: SpecialityService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.specialityService.getSpecialities({
      nameEn: this.filters.nameEn.trim() || undefined,
      nameAr: this.filters.nameAr.trim() || undefined,
    }).subscribe({
      next: (data) => { this.list = data ?? []; this.loading = false; },
      error: () => { this.loading = false; },
    });
  }

  applyFilters(): void {
    this.load();
  }

  clearFilters(): void {
    this.filters = { nameEn: '', nameAr: '' };
    this.load();
  }

  delete(s: SpecialityResponse): void {
    if (!s.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteSpeciality' })
      .subscribe((ok) => {
        if (!ok) return;
        this.specialityService.deleteSpeciality(s.id!).subscribe({
          next: () => this.load(),
        });
      });
  }
}
