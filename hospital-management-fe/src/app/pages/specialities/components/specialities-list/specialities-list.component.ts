import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { SpecialityService } from '../../services/speciality.service';
import type { SpecialityResponse } from '../../models/response/speciality-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { DEFAULT_PAGE_SIZE_OPTIONS } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-specialities-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, ListFilterToggleComponent, ListPaginationComponent],
  templateUrl: './specialities-list.component.html',
  styleUrls: ['./specialities-list.component.css'],
})
export class SpecialitiesListComponent implements OnInit {
  list: SpecialityResponse[] = [];
  totalElements = 0;
  loading = false;
  showFilters = false;
  filters = { nameEn: '', nameAr: '' };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

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
      page: this.currentPage - 1,
      size: this.pageSize,
    }).subscribe({
      next: (response) => {
        this.list = response.data ?? [];
        this.totalElements = response.totalElements ?? 0;
        this.currentPage = (response.page ?? 0) + 1;
        this.pageSize = response.size ?? this.pageSize;
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
    this.filters = { nameEn: '', nameAr: '' };
    this.currentPage = 1;
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
