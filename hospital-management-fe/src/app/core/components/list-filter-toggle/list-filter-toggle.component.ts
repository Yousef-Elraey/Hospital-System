import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-list-filter-toggle',
  standalone: true,
  imports: [TranslateModule],
  templateUrl: './list-filter-toggle.component.html',
})
export class ListFilterToggleComponent {
  @Input() expanded = false;
  @Output() expandedChange = new EventEmitter<boolean>();

  toggle(): void {
    this.expanded = !this.expanded;
    this.expandedChange.emit(this.expanded);
  }
}
