import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  forwardRef,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import type { Options } from '@popperjs/core';
import {
  NgbCalendar,
  NgbDate,
  NgbDateParserFormatter,
  NgbDatepickerI18n,
  NgbDatepickerModule,
  NgbInputDatepicker,
  NgbDateStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import type { Lang } from '../../../../core/services/locale.service';
import { formatDateDisplay, parseDisplayDateToIso } from '../../../../core/utils/display-date';
import { HospitalDateParserFormatter } from './hospital-date-parser-formatter';
import { HospitalNgbDatepickerI18n } from './hospital-ngb-datepicker-i18n';

/** Popper modifier: set popup width to match the reference (date field row). */
/** Lower bound for selectable dates when `[minDate]` is omitted (ng-bootstrap uses viewYear−10 otherwise). */
const DEFAULT_DATEPICKER_MIN = new NgbDate(1900, 1, 1);

function pad2(n: number): string {
  return String(n).padStart(2, '0');
}

function mergeDatepickerPopperOptions(options: Partial<Options>): Partial<Options> {
  const sameWidth = {
    name: 'sameWidth',
    enabled: true,
    phase: 'beforeWrite' as const,
    requires: ['computeStyles'],
    fn: ({ state }: { state: any }) => {
      const w = `${state.rects.reference.width}px`;
      if (state.styles.popper) {
        state.styles.popper['width'] = w;
      }
    },
  };
  return {
    ...options,
    modifiers: [...(options.modifiers ?? []), sameWidth as any],
  };
}

@Component({
  selector: 'app-hospital-datepicker',
  standalone: true,
  imports: [CommonModule, FormsModule, NgbDatepickerModule],
  templateUrl: './hospital-datepicker.component.html',
  styleUrl: './hospital-datepicker.component.css',
  providers: [
    { provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => HospitalDatepickerComponent), multi: true },
    { provide: NgbDatepickerI18n, useClass: HospitalNgbDatepickerI18n },
    { provide: NgbDateParserFormatter, useClass: HospitalDateParserFormatter },
  ],
})
export class HospitalDatepickerComponent implements ControlValueAccessor, OnInit, OnDestroy {
  @ViewChild('fieldAnchor', { read: ElementRef }) fieldAnchor?: ElementRef<HTMLElement>;
  @ViewChild('dateInput', { read: ElementRef }) dateInput?: ElementRef<HTMLElement>;

  constructor(
    private host: ElementRef<HTMLElement>,
    private calendar: NgbCalendar,
    private translate: TranslateService,
    private cdr: ChangeDetectorRef,
  ) {}

  /** If empty, placeholder is DD-MM-YYYY (same pattern for en and ar; ar uses RTL + eastern digits in the value). */
  @Input() placeholder?: string;
  @Input() name = '';
  @Input() maxDate?: NgbDate | null;
  @Input() minDate?: NgbDate | null;
  @Input() disabled = false;
  @Input() readonly = true;
  @Input() containerClass = '';
  @Input() inputClass: string | Record<string, boolean | null | undefined> = '';
  @Input() errorMessage = '';

  @Output() dateChange = new EventEmitter<string>();

  /**
   * Model for the inner `input[ngbDatepicker]` — must be `NgbDateStruct | null`.
   * `NgbInputDatepicker` emits structs; binding a string caused `[object Object]` on first pick.
   */
  innerDate: NgbDateStruct | null = null;

  private langChangeSubscription?: Subscription;
  onChange: (value: string) => void = () => {};
  onTouched: () => void = () => {};

  readonly datepickerPopperOptions = (options: Partial<Options>): Partial<Options> =>
    mergeDatepickerPopperOptions(options);

  ngOnInit(): void {
    this.langChangeSubscription = this.translate.onLangChange.subscribe((e) => {
      const newLang = e.lang as Lang;
      if (this.innerDate != null) {
        const iso = `${this.innerDate.year}-${pad2(this.innerDate.month)}-${pad2(this.innerDate.day)}`;
        this.onChange(formatDateDisplay(iso, newLang));
      }
      this.cdr.detectChanges();
    });
  }

  ngOnDestroy(): void {
    this.langChangeSubscription?.unsubscribe();
  }

  writeValue(value: string | null): void {
    const v = String(value ?? '').trim();
    if (!v) {
      this.innerDate = null;
      return;
    }
    const lang = (this.translate.currentLang || this.translate.getDefaultLang() || 'en') as Lang;
    const head = v.slice(0, 10);
    let iso: string | null = null;
    if (/^\d{4}-\d{2}-\d{2}$/.test(head)) {
      iso = head;
    } else {
      iso = parseDisplayDateToIso(v, lang) || parseDisplayDateToIso(v, lang === 'ar' ? 'en' : 'ar');
    }
    if (!iso) {
      this.innerDate = null;
      return;
    }
    const [y, m, d] = iso.split('-').map(Number);
    this.innerDate = { year: y, month: m, day: d };
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onInnerDateChange(model: NgbDateStruct | null): void {
    this.innerDate = model;
    const lang = (this.translate.currentLang || this.translate.getDefaultLang() || 'en') as Lang;
    if (
      !model ||
      model.year == null ||
      model.month == null ||
      model.day == null ||
      !Number.isInteger(model.year) ||
      !Number.isInteger(model.month) ||
      !Number.isInteger(model.day)
    ) {
      this.onChange('');
      this.onTouched();
      this.dateChange.emit('');
      return;
    }
    const iso = `${model.year}-${pad2(model.month)}-${pad2(model.day)}`;
    const display = formatDateDisplay(iso, lang);
    const out = display === '-' ? '' : display;
    this.onChange(out);
    this.onTouched();
    this.dateChange.emit(out);
  }

  /**
   * Open from keyboard only. Do not use (focus) to open: ng-bootstrap restores focus to this input
   * after a date is chosen, which would immediately reopen the popup.
   */
  onDateInputKeydown(event: KeyboardEvent, dp: NgbInputDatepicker): void {
    if (this.disabled) return;
    if (event.key === 'ArrowDown' || event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      dp.open();
    }
  }

  getAriaLabel(): string {
    const lang = this.translate.currentLang || this.translate.getDefaultLang() || 'en';
    return lang === 'ar' ? 'فتح التقويم' : 'Open calendar';
  }

  get isRTL(): boolean {
    const lang = this.translate.currentLang || this.translate.getDefaultLang() || 'en';
    return lang === 'ar';
  }

  get effectivePlaceholder(): string {
    const custom = (this.placeholder ?? '').trim();
    if (custom) return custom;
    return 'DD-MM-YYYY';
  }

  /** Bounds passed to ngbDatepicker; defaults suit DOB and general use (year list 1900 → max). */
  get effectiveMinDate(): NgbDate {
    return this.minDate ?? DEFAULT_DATEPICKER_MIN;
  }

  get effectiveMaxDate(): NgbDate {
    return this.maxDate ?? this.calendar.getToday();
  }

  /** Input width drives popup width so it matches only the text field. */
  get datepickerPositionTarget(): HTMLElement {
    return (
      this.dateInput?.nativeElement ??
      this.fieldAnchor?.nativeElement ??
      (this.host.nativeElement.querySelector('.form-control') as HTMLElement) ??
      (this.host.nativeElement.querySelector('.input-group') as HTMLElement)
    );
  }
}
