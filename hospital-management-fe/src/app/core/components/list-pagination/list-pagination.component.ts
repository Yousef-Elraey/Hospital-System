import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { DEFAULT_PAGE_SIZE_OPTIONS, pageNumbers } from '../../utils/list-pagination';

@Component({
  selector: 'app-list-pagination',
  standalone: true,
  imports: [FormsModule, TranslateModule],
  templateUrl: './list-pagination.component.html',
})
export class ListPaginationComponent {
  @Input() totalItems = 0;
  @Input() pageSize = 10;
  @Input() currentPage = 1;
  @Input() pageSizeOptions: readonly number[] = DEFAULT_PAGE_SIZE_OPTIONS;
  @Input() totalCountKey = 'common.totalRecords';
  @Input() paginationLabelKey = 'common.pagination';
  @Input() selectName = 'listPageSize';

  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();

  get pages(): number[] {
    return pageNumbers(this.totalItems, this.pageSize);
  }

  onPageChange(page: number): void {
    if (page < 1 || page > this.pages.length) return;
    this.pageChange.emit(page);
  }

  onPageSizeChange(size: number): void {
    this.pageSizeChange.emit(size);
  }
}
