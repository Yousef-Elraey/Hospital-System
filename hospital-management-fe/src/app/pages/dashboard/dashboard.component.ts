import { Component } from '@angular/core';
import { NgStyle } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import type { IconName } from '../../core/icons/icons';
import { IconComponent } from '../../core/components/icon/icon.component';
import { MODULE_COLORS, moduleIconStyle } from '../../core/theme/module-colors';

export interface DashboardLink {
  to: string;
  labelKey: string;
  descKey: string;
  icon: IconName;
  color: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, TranslateModule, IconComponent, NgStyle],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  readonly iconStyle = moduleIconStyle;

  links: DashboardLink[] = [
    { to: '/patients', labelKey: 'breadcrumb.patients', descKey: 'dashboard.desc.patients', icon: 'patients', color: MODULE_COLORS['patients'] },
    { to: '/doctors', labelKey: 'breadcrumb.doctors', descKey: 'dashboard.desc.doctors', icon: 'doctors', color: MODULE_COLORS['doctors'] },
    { to: '/appointments', labelKey: 'breadcrumb.appointments', descKey: 'dashboard.desc.appointments', icon: 'appointments', color: MODULE_COLORS['appointments'] },
    { to: '/appointment-slots', labelKey: 'breadcrumb.appointmentSlots', descKey: 'dashboard.desc.appointmentSlots', icon: 'appointmentSlots', color: MODULE_COLORS['appointmentSlots'] },
    { to: '/reports', labelKey: 'breadcrumb.reports', descKey: 'dashboard.desc.reports', icon: 'reports', color: MODULE_COLORS['reports'] },
    { to: '/billing', labelKey: 'breadcrumb.billing', descKey: 'dashboard.desc.billing', icon: 'billing', color: MODULE_COLORS['billing'] },
    { to: '/medical-records', labelKey: 'breadcrumb.medicalRecords', descKey: 'dashboard.desc.medicalRecords', icon: 'medicalRecords', color: MODULE_COLORS['medicalRecords'] },
  ];
}
