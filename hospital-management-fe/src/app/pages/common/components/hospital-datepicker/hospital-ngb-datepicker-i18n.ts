import { Injectable } from '@angular/core';
import {
  FormStyle,
  TranslationWidth,
  formatDate,
  getLocaleDayNames,
  getLocaleMonthNames,
} from '@angular/common';
import { NgbDatepickerI18n, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { toArabicIndicDigits } from '../../../../core/utils/display-date';

/** ISO weekdays: 1 = Monday … 7 = Sunday (matches NgbCalendarGregorian.getWeekday). */
function weekdaysMondayFirst(locale: string, width: TranslationWidth): string[] {
  const sunFirst = getLocaleDayNames(locale, FormStyle.Standalone, width);
  return sunFirst.map((_, i) => sunFirst[(i + 1) % 7]);
}

@Injectable()
export class HospitalNgbDatepickerI18n extends NgbDatepickerI18n {
  constructor(private translate: TranslateService) {
    super();
  }

  private localeId(): string {
    const lang = this.translate.currentLang || this.translate.getDefaultLang() || 'en';
    return lang === 'ar' ? 'ar' : 'en-US';
  }

  private useEasternArabicNumerals(): boolean {
    return this.localeId() === 'ar';
  }

  override getDayNumerals(date: NgbDateStruct): string {
    const s = String(date.day);
    return this.useEasternArabicNumerals() ? toArabicIndicDigits(s) : s;
  }

  override getYearNumerals(year: number): string {
    const s = String(year);
    return this.useEasternArabicNumerals() ? toArabicIndicDigits(s) : s;
  }

  override getWeekNumerals(weekNumber: number): string {
    const s = String(weekNumber);
    return this.useEasternArabicNumerals() ? toArabicIndicDigits(s) : s;
  }

  getWeekdayLabel(weekday: number, width?: TranslationWidth): string {
    const locale = this.localeId();
    const w =
      width ??
      (locale === 'ar' ? TranslationWidth.Narrow : TranslationWidth.Short);
    const labels = weekdaysMondayFirst(locale, w);
    return labels[weekday - 1] || '';
  }

  getMonthShortName(month: number, _year?: number): string {
    const locale = this.localeId();
    const names = getLocaleMonthNames(locale, FormStyle.Standalone, TranslationWidth.Abbreviated);
    return names[month - 1] || '';
  }

  getMonthFullName(month: number, _year?: number): string {
    const locale = this.localeId();
    const names = getLocaleMonthNames(locale, FormStyle.Standalone, TranslationWidth.Wide);
    return names[month - 1] || '';
  }

  getDayAriaLabel(date: NgbDateStruct): string {
    const locale = this.localeId();
    const js = new Date(date.year, date.month - 1, date.day);
    return formatDate(js, 'fullDate', locale);
  }
}
