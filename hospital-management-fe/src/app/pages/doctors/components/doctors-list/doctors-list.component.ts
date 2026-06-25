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
import { LocaleService } from '../../../../core/services/locale.service';
import { DEFAULT_PAGE_SIZE_OPTIONS, applyPageResponse, toPageRequest } from '../../../../core/utils/list-pagination';
import { specialityDisplayName } from '../../utils/speciality-display-name';

@Component({
  selector: 'app-doctors-list',
  standalone: true,
  imports: [RouterLink, FormsModule, TranslateModule, PageHeaderComponent, ListFilterToggleComponent, ListPaginationComponent],
  templateUrl: './doctors-list.component.html',
  styleUrls: ['./doctors-list.component.css'],
})
export class DoctorsListComponent implements OnInit {
  list: DoctorResponse[] = [];
  totalElements = 0;
  loading = false;
  showFilters = false;
  filters = { name: '', speciality: '', contactNumber: '' };
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

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
    this.doctorService.getDoctors({
      name: this.filters.name.trim() || undefined,
      speciality: this.filters.speciality.trim() || undefined,
      contactNumber: this.filters.contactNumber.trim() || undefined,
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
    this.filters = { name: '', speciality: '', contactNumber: '' };
    this.currentPage = 1;
    this.load();
  }

  specialityName(doctor: DoctorResponse): string {
    return specialityDisplayName(doctor.speciality, this.locale.currentLang);
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
