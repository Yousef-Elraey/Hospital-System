import { Component } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, ActivatedRoute } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { map, startWith } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { LocaleService } from '../../services/locale.service';
import { TranslateService } from '@ngx-translate/core';
import { IconComponent } from '../icon/icon.component';

export interface BreadcrumbItem {
  labelKey: string;
  path: string;
}

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, TranslateModule, AsyncPipe, IconComponent],
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css'],
})
export class BreadcrumbComponent {
  breadcrumbs$: Observable<BreadcrumbItem[]>;
  isRtl = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private locale: LocaleService,
    private translate: TranslateService
  ) {
    this.isRtl = this.locale.isRtl;
    this.translate.onLangChange.subscribe(() => (this.isRtl = this.locale.isRtl));
    this.breadcrumbs$ = this.router.events.pipe(
      startWith(null),
      map(() => this.buildBreadcrumbs())
    );
  }

  private buildBreadcrumbs(): BreadcrumbItem[] {
    const url = this.router.url.split('?')[0];
    const segments = url.split('/').filter(s => s !== '');
    const items: BreadcrumbItem[] = [];
    let path = '';
    for (const seg of segments) {
      path = path ? `${path}/${seg}` : `/${seg}`;
      items.push({ labelKey: this.getLabelKey(seg), path });
    }
    return items;
  }

  private getLabelKey(path: string): string {
    const map: Record<string, string> = {
      '': 'breadcrumb.home',
      patients: 'breadcrumb.patients',
      doctors: 'breadcrumb.doctors',
      specialities: 'breadcrumb.specialities',
      appointments: 'breadcrumb.appointments',
      'appointment-slots': 'breadcrumb.appointmentSlots',
      billing: 'breadcrumb.billing',
      reports: 'breadcrumb.reports',
      'medical-records': 'breadcrumb.medicalRecords',
      add: 'breadcrumb.add',
      edit: 'breadcrumb.edit',
      book: 'breadcrumb.book',
      slots: 'breadcrumb.slots',
    };
    if (map[path]) return map[path];
    if (/^\d+$/.test(path)) return 'breadcrumb.view';
    return path;
  }
}
