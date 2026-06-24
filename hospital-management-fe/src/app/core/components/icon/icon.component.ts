import { Component, HostBinding, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FA_ICONS, IconName } from '../../icons/icons';

export type IconSize = 'sm' | 'md' | 'lg' | 'xl';

@Component({
  selector: 'app-icon',
  standalone: true,
  template: `<i [class]="iconClass" aria-hidden="true"></i>`,
  styles: [
    `
      :host {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        line-height: 1;
        flex-shrink: 0;
      }
      i {
        display: inline-flex;
        align-items: center;
        justify-content: center;
      }
      :host(.icon-sm) i { font-size: 0.8rem; }
      :host(.icon-md) i { font-size: 1rem; }
      :host(.icon-lg) i { font-size: 1.25rem; }
      :host(.icon-xl) i { font-size: 1.5rem; }
    `,
  ],
})
export class IconComponent implements OnChanges, OnInit {
  @Input({ required: true }) name!: IconName;
  @Input() size: IconSize = 'md';
  iconClass = '';

  @HostBinding('class')
  get sizeClass(): string {
    return `icon-${this.size}`;
  }

  private updateIcon(): void {
    this.iconClass = this.name ? (FA_ICONS[this.name] ?? '') : '';
  }

  ngOnInit(): void {
    this.updateIcon();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['name']) {
      this.updateIcon();
    }
  }
}
