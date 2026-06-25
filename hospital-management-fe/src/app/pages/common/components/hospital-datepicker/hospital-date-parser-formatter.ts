import { Injectable } from '@angular/core';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import type { Lang } from '../../../../core/services/locale.service';
import { formatDateDisplay, parseDisplayDateToIso } from '../../../../core/utils/display-date';

function pad2(n: number): string {
  return String(n).padStart(2, '0');
}

@Injectable()
export class HospitalDateParserFormatter extends NgbDateParserFormatter {
  constructor(private translate: TranslateService) {
    super();
  }

  private get lang(): Lang {
    return (this.translate.currentLang || this.translate.getDefaultLang() || 'en') as Lang;
  }

  parse(value: string): NgbDateStruct | null {
    const iso = parseDisplayDateToIso(value == null ? null : String(value), this.lang);
    if (!iso) return null;
    const [y, m, d] = iso.split('-').map(Number);
    return { year: y, month: m, day: d };
  }

  format(date: NgbDateStruct | null): string {
    if (!date?.year || date.month == null || date.day == null) return '';
    if (date.month < 1 || date.day < 1) return '';
    const iso = `${date.year}-${pad2(date.month)}-${pad2(date.day)}`;
    const out = formatDateDisplay(iso, this.lang);
    return out === '-' ? '' : out;
  }
}
