import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DoctorService } from '../../services/doctor.service';
import type { DoctorResponse } from '../../models/response/doctor-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { ListFilterToggleComponent } from '../../../../core/components/list-filter-toggle/list-filter-toggle.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import {
  clampPage,
  DEFAULT_PAGE_SIZE_OPTIONS,
  paginate,
} from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-doctors-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, ListFilterToggleComponent, ListPaginationComponent],
  templateUrl: './doctors-list.component.html',
  styleUrls: ['./doctors-list.component.css'],
})
export class DoctorsListComponent implements OnInit {
  list: DoctorResponse[] = [];
  loading = false;
  showFilters = false;
  filters = { name: '', speciality: '', contactNumber: '' };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

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
      next: (data) => {
        this.list = data ?? [];
        this.loading = false;
        this.currentPage = clampPage(this.currentPage, this.list.length, this.pageSize);
      },
      error: () => { this.loading = false; },
    });
  }

  get pagedList(): DoctorResponse[] {
    return paginate(this.list, this.currentPage, this.pageSize);
  }

  setPage(page: number): void {
    this.currentPage = page;
  }

  setPageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 1;
  }

  applyFilters(): void {
    this.currentPage = 1;
    this.load();
  }

  clearFilters(): void {
    this.filters = { name: '', speciality: '', contactNumber: '' };
    this.currentPage = 1;
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
