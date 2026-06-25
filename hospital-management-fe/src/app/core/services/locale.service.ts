import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';

export type Lang = 'en' | 'ar';

const LANG_KEY = 'hospital_lang';

export type Dir = 'ltr' | 'rtl';

@Injectable({ providedIn: 'root' })
export class LocaleService {
  private isBrowser: boolean;
  /** Set synchronously so layout/UI can bind to it immediately (translate.use is async). */
  direction: Dir = 'ltr';

  constructor(
    private translate: TranslateService,
    @Inject(PLATFORM_ID) platformId: object,
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    const saved = this.isBrowser ? (localStorage.getItem(LANG_KEY) as Lang) : null;
    const lang = saved === 'ar' || saved === 'en' ? saved : 'en';
    this.direction = lang === 'ar' ? 'rtl' : 'ltr';
    this.translate.use(lang);
    this.translate.setDefaultLang('en');
    this.applyDirection(lang);
  }

  get currentLang(): Lang {
    return (this.translate.currentLang || 'en') as Lang;
  }

  get isRtl(): boolean {
    return this.direction === 'rtl';
  }

  setLanguage(lang: Lang): void {
    this.direction = lang === 'ar' ? 'rtl' : 'ltr';
    if (this.isBrowser) {
      localStorage.setItem(LANG_KEY, lang);
    }
    this.applyDirection(lang);
    this.translate.use(lang);
  }

  private applyDirection(lang: Lang): void {
    if (!this.isBrowser) return;
    const dir = lang === 'ar' ? 'rtl' : 'ltr';
    const html = document.documentElement;
    html.setAttribute('dir', dir);
    html.setAttribute('lang', lang);
  }
}
