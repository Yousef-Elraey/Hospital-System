import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TreatmentService } from '../../services/treatment.service';
import type { TreatmentResponse } from '../../models/response/treatment-response.dto';
import { PageHeaderComponent } from '../../../../core/components/page-header/page-header.component';
import { ListPaginationComponent } from '../../../../core/components/list-pagination/list-pagination.component';
import { ConfirmDialogService } from '../../../../core/services/confirm-dialog.service';
import { DEFAULT_PAGE_SIZE_OPTIONS, applyPageResponse, toPageRequest } from '../../../../core/utils/list-pagination';

@Component({
  selector: 'app-treatments-list',
  standalone: true,
  imports: [RouterLink, TranslateModule, PageHeaderComponent, ListPaginationComponent],
  templateUrl: './treatments-list.component.html',
  styleUrls: ['./treatments-list.component.css'],
})
export class TreatmentsListComponent implements OnInit {
  list: TreatmentResponse[] = [];
  totalElements = 0;
  loading = false;
  readonly pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  pageSize = 10;
  currentPage = 1;

  constructor(
    private treatmentService: TreatmentService,
    private confirmDialog: ConfirmDialogService,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.treatmentService.getTreatments(toPageRequest(this.currentPage, this.pageSize)).subscribe({
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

  delete(t: TreatmentResponse): void {
    if (!t.id) return;
    this.confirmDialog
      .ask({ titleKey: 'confirm.title', messageKey: 'confirm.deleteTreatment' })
      .subscribe((ok) => {
        if (!ok) return;
        this.treatmentService.deleteTreatment(t.id!).subscribe({
          next: () => this.load(),
        });
      });
  }
}
