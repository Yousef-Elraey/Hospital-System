import { Component } from '@angular/core';
import { AsyncPipe, Location } from '@angular/common';
import { NavigationEnd, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { filter, map, startWith } from 'rxjs/operators';
import { LocaleService } from '../../services/locale.service';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'app-back-button',
  standalone: true,
  imports: [TranslateModule, IconComponent, AsyncPipe],
  templateUrl: './back-button.component.html',
  styleUrls: ['./back-button.component.css'],
})
export class BackButtonComponent {
  showBack$ = this.router.events.pipe(
    filter((e): e is NavigationEnd => e instanceof NavigationEnd),
    startWith(null),
    map(() => {
      const path = this.router.url.split('?')[0];
      return path !== '/' && path !== '';
    })
  );

  get isRtl(): boolean {
    return this.locale.isRtl;
  }

  constructor(
    private location: Location,
    private locale: LocaleService,
    private router: Router,
  ) {}

  goBack(): void {
    this.location.back();
  }
}
