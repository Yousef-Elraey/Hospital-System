import { Component, ChangeDetectorRef, ViewEncapsulation } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '../../auth/auth.service';
import { LocaleService } from '../../services/locale.service';
import type { IconName } from '../../icons/icons';
import { MODULE_COLORS } from '../../theme/module-colors';
import { BackButtonComponent } from '../back-button/back-button.component';
import { BreadcrumbComponent } from '../breadcrumb/breadcrumb.component';
import { IconComponent } from '../icon/icon.component';

export interface NavItem {
  route: string;
  labelKey: string;
  icon: IconName;
  color: string;
  exact?: boolean;
}

@Component({
  selector: 'app-layout',
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    TranslateModule,
    BackButtonComponent,
    BreadcrumbComponent,
    IconComponent,
  ],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
})
export class LayoutComponent {
  currentYear = new Date().getFullYear();

  readonly navItems: NavItem[] = [
    { route: '/', labelKey: 'breadcrumb.home', icon: 'home', color: MODULE_COLORS['home'], exact: true },
    { route: '/patients', labelKey: 'nav.patients', icon: 'patients', color: MODULE_COLORS['patients'] },
    { route: '/doctors', labelKey: 'nav.doctors', icon: 'doctors', color: MODULE_COLORS['doctors'] },
    { route: '/appointments', labelKey: 'nav.appointments', icon: 'appointments', color: MODULE_COLORS['appointments'] },
    { route: '/appointment-slots', labelKey: 'nav.appointmentSlots', icon: 'appointmentSlots', color: MODULE_COLORS['appointmentSlots'] },
    { route: '/reports', labelKey: 'nav.reports', icon: 'reports', color: MODULE_COLORS['reports'] },
    { route: '/billing', labelKey: 'nav.billing', icon: 'billing', color: MODULE_COLORS['billing'] },
    { route: '/medical-records', labelKey: 'nav.medicalRecords', icon: 'medicalRecord', color: MODULE_COLORS['medicalRecords'] },
    { route: '/specialities', labelKey: 'nav.specialities', icon: 'specialities', color: MODULE_COLORS['specialities'] },
  ];

  constructor(
    public auth: AuthService,
    public locale: LocaleService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  setLang(lang: 'en' | 'ar'): void {
    this.locale.setLanguage(lang);
    this.cdr.detectChanges();
  }
}
