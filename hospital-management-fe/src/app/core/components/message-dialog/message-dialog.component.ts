import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { TranslateModule } from '@ngx-translate/core';

export interface MessageDialogData {
  variant: 'success' | 'error';
  message: string;
}

@Component({
  standalone: true,
  selector: 'app-message-dialog',
  imports: [MatDialogModule, MatButtonModule, TranslateModule],
  template: `
    <mat-dialog-content class="dialog-content">
      <div
        class="icon-circle"
        [class.icon-circle--success]="data.variant === 'success'"
        [class.icon-circle--error]="data.variant === 'error'"
        aria-hidden="true"
      >
        @if (data.variant === 'success') {
          <i class="fa-solid fa-circle-check"></i>
        } @else {
          <i class="fa-solid fa-circle-exclamation"></i>
        }
      </div>
      <h2 class="dialog-title" [class.title-success]="data.variant === 'success'" [class.title-error]="data.variant === 'error'">
        {{ (data.variant === 'success' ? 'apiFeedback.successTitle' : 'apiFeedback.errorTitle') | translate }}
      </h2>
      <p class="dialog-body">{{ data.message }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-flat-button [color]="data.variant === 'success' ? 'primary' : 'warn'" (click)="close()">
        {{ 'apiFeedback.ok' | translate }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      .dialog-content {
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
        padding-top: 0.5rem;
      }
      .icon-circle {
        width: 4.5rem;
        height: 4.5rem;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 1rem;
        font-size: 2.25rem;
      }
      .icon-circle--success {
        background: var(--success-muted, rgba(22, 163, 74, 0.12));
        color: var(--success, #16a34a);
      }
      .icon-circle--error {
        background: var(--danger-muted, rgba(220, 38, 38, 0.1));
        color: var(--danger, #dc2626);
      }
      .dialog-title {
        margin: 0 0 0.75rem;
        font-size: 1.25rem;
        font-weight: 600;
        width: 100%;
      }
      .title-success {
        color: var(--success, #16a34a);
      }
      .title-error {
        color: var(--danger, #dc2626);
      }
      .dialog-body {
        margin: 0;
        white-space: pre-wrap;
        width: 100%;
        color: var(--text, #1e293b);
        line-height: 1.5;
      }
    `,
  ],
})
export class MessageDialogComponent {
  readonly data = inject<MessageDialogData>(MAT_DIALOG_DATA);
  private readonly ref = inject(MatDialogRef<MessageDialogComponent>);

  close(): void {
    this.ref.close();
  }
}
