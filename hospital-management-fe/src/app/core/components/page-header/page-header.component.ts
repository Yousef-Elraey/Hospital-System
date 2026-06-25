import { Component, Input } from '@angular/core';
import { NgStyle } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import type { IconName } from '../../icons/icons';
import { moduleColorForIcon, moduleIconStyle } from '../../theme/module-colors';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [TranslateModule, IconComponent, NgStyle],
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.css'],
})
export class PageHeaderComponent {
  @Input() titleKey!: string;
  @Input() subtitleKey = '';
  @Input() iconName!: IconName;
  /** Optional override; defaults to the module palette. */
  @Input() iconColor?: string;

  get resolvedIconColor(): string {
    return this.iconColor ?? moduleColorForIcon(this.iconName);
  }

  get iconBgStyle(): Record<string, string> {
    return moduleIconStyle(this.resolvedIconColor);
  }
}
