import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LocaleService } from './core/services/locale.service';
import { HttpLoadingService } from './core/services/http-loading.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatProgressSpinnerModule],
  template: `
    <router-outlet />
    @if (httpLoading.active()) {
      <div class="http-loading-overlay" role="status" aria-live="polite" aria-busy="true">
        <mat-spinner diameter="48"></mat-spinner>
      </div>
    }
  `,
  styles: [
    `
      :host {
        display: block;
        min-height: 100vh;
      }
      .http-loading-overlay {
        position: fixed;
        inset: 0;
        z-index: 900;
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(255, 255, 255, 0.55);
        backdrop-filter: blur(2px);
      }
    `,
  ],
})
export class AppComponent {
  constructor(
    private locale: LocaleService,
    readonly httpLoading: HttpLoadingService,
  ) {}
}
