import { Component, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { LocaleService } from '../../../../core/services/locale.service';

@Component({
  selector: 'app-appointment-time-picker',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './appointment-time-picker.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AppointmentTimePickerComponent),
      multi: true,
    },
  ],
})
export class AppointmentTimePickerComponent implements ControlValueAccessor {
  hour12: number | null = null;
  minute: number | null = null;
  meridian: 'AM' | 'PM' = 'AM';

  readonly hourOptions = Array.from({ length: 12 }, (_, i) => i + 1);
  readonly minuteOptions = [0, 10, 20, 30, 40, 50];

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(public locale: LocaleService) {}

  writeValue(value: string | null): void {
    const match = /^(\d{2}):(\d{2})$/.exec(value ?? '');
    if (!match) {
      this.hour12 = null;
      this.minute = null;
      this.meridian = 'AM';
      return;
    }

    const hour24 = Number(match[1]);
    const minute = Number(match[2]);
    this.meridian = hour24 >= 12 ? 'PM' : 'AM';
    this.hour12 = hour24 % 12 || 12;
    this.minute = Math.floor(minute / 10) * 10;
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(_isDisabled: boolean): void {}

  onTimePartChange(): void {
    if (this.hour12 == null || this.minute == null) {
      this.onChange('');
      this.onTouched();
      return;
    }

    let hour24 = this.hour12 % 12;
    if (this.meridian === 'PM') hour24 += 12;
    const value = `${String(hour24).padStart(2, '0')}:${String(this.minute).padStart(2, '0')}`;
    this.onChange(value);
    this.onTouched();
  }

  formatLocalizedNumber(value: number): string {
    const twoDigits = String(value).padStart(2, '0');
    if (this.locale.currentLang === 'ar') {
      return twoDigits.replace(/\d/g, (digit) => '٠١٢٣٤٥٦٧٨٩'[Number(digit)]);
    }
    return twoDigits;
  }
}
