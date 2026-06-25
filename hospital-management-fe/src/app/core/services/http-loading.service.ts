import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class HttpLoadingService {
  private pending = 0;
  private readonly _active = signal(false);
  /** True while at least one API request is in flight. */
  readonly active = this._active.asReadonly();

  begin(): void {
    this.pending++;
    this._active.set(true);
  }

  end(): void {
    this.pending = Math.max(0, this.pending - 1);
    this._active.set(this.pending > 0);
  }
}
