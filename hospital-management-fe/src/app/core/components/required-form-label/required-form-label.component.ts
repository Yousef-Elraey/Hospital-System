import { Component, Input } from '@angular/core';
import { NgClass } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-required-form-label',
  standalone: true,
  imports: [TranslateModule, NgClass],
  template: `<label class="form-label" [ngClass]="labelClass" [attr.for]="htmlFor"><span class="form-label-required-text">{{ labelKey | translate }}<span class="form-label-required-asterisk" aria-hidden="true">*</span></span></label>`,
})
export class RequiredFormLabelComponent {
  @Input({ required: true }) labelKey!: string;
  @Input() htmlFor?: string;
  @Input() labelClass: string | string[] | Record<string, boolean> = '';
}
