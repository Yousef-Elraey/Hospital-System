import { Component, ChangeDetectorRef, ViewEncapsulation, HostListener, OnInit, OnDestroy, inject } from '@angular/core';

import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';

import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

import { filter, Subscription } from 'rxjs';

import { AuthService } from '../../auth/auth.service';

import { LocaleService } from '../../services/locale.service';

import type { IconName } from '../../icons/icons';

import { HM_DESKTOP_MIN_WIDTH } from '../../theme/breakpoints';
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

    NgbDropdownModule,

  ],

  templateUrl: './layout.component.html',

  styleUrls: ['./layout.component.css'],

})

export class LayoutComponent implements OnInit, OnDestroy {

  currentYear = new Date().getFullYear();

  sidebarOpen = false;



  private readonly desktopSidebarMinWidth = HM_DESKTOP_MIN_WIDTH;

  private navSubscription?: Subscription;



  readonly navItems: NavItem[] = [

    { route: '/', labelKey: 'breadcrumb.home', icon: 'home', color: MODULE_COLORS['home'], exact: true },

    { route: '/medical-records', labelKey: 'nav.medicalRecords', icon: 'medicalRecord', color: MODULE_COLORS['medicalRecords'] },


    { route: '/appointments', labelKey: 'nav.appointments', icon: 'appointments', color: MODULE_COLORS['appointments'] },



    { route: '/billing', labelKey: 'nav.billing', icon: 'billing', color: MODULE_COLORS['billing'] },

    { route: '/doctors', labelKey: 'nav.doctors', icon: 'doctors', color: MODULE_COLORS['doctors'] },
    { route: '/patients', labelKey: 'nav.patients', icon: 'patients', color: MODULE_COLORS['patients'] },
    { route: '/treatments', labelKey: 'nav.treatments', icon: 'treatments', color: MODULE_COLORS['treatments'] },
    { route: '/diagnoses', labelKey: 'nav.diagnoses', icon: 'diagnoses', color: MODULE_COLORS['diagnoses'] },
    { route: '/specialities', labelKey: 'nav.specialities', icon: 'specialities', color: MODULE_COLORS['specialities'] },
    { route: '/reports', labelKey: 'nav.reports', icon: 'reports', color: MODULE_COLORS['reports'] },
    { route: '/appointment-slots', labelKey: 'nav.appointmentSlots', icon: 'appointmentSlots', color: MODULE_COLORS['appointmentSlots'] },

  ];



  private router = inject(Router);



  constructor(

    public auth: AuthService,

    public locale: LocaleService,

    private cdr: ChangeDetectorRef

  ) {}



  ngOnInit(): void {

    this.navSubscription = this.router.events

      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))

      .subscribe(() => this.closeSidebar());

  }



  ngOnDestroy(): void {

    this.navSubscription?.unsubscribe();

    this.setBodyScrollLocked(false);

  }



  @HostListener('window:resize')

  onResize(): void {

    if (this.isDesktopViewport()) {

      this.closeSidebar();

    }

  }



  @HostListener('document:keydown.escape')

  onEscape(): void {

    this.closeSidebar();

  }



  toggleSidebar(): void {

    this.sidebarOpen = !this.sidebarOpen;

    this.setBodyScrollLocked(this.sidebarOpen && !this.isDesktopViewport());

  }



  closeSidebar(): void {

    if (!this.sidebarOpen) {

      return;

    }

    this.sidebarOpen = false;

    this.setBodyScrollLocked(false);

  }



  get displayName(): string {
    return this.auth.username?.trim() ?? '';
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }



  setLang(lang: 'en' | 'ar'): void {

    this.locale.setLanguage(lang);

    this.cdr.detectChanges();

  }



  private isDesktopViewport(): boolean {

    return typeof window !== 'undefined' && window.innerWidth >= this.desktopSidebarMinWidth;

  }



  private setBodyScrollLocked(locked: boolean): void {

    document.body.classList.toggle('sidebar-scroll-lock', locked);

  }

}


