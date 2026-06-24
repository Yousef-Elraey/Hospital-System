import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { TranslateModule } from '@ngx-translate/core';

export interface ConfirmDialogData {
  titleKey: string;
  messageKey: string;
}

@Component({
  standalone: true,
  selector: 'app-confirm-dialog',
  imports: [MatDialogModule, MatButtonModule, TranslateModule],
  template: `
    <h2 mat-dialog-title class="dialog-title">
      <span class="title-icon" aria-hidden="true"><i class="fa-solid fa-triangle-exclamation"></i></span>
      {{ data.titleKey | translate }}
    </h2>
    <mat-dialog-content class="dialog-content">
      <p>{{ data.messageKey | translate }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button type="button" (click)="cancel()">{{ 'confirm.cancel' | translate }}</button>
      <button mat-flat-button color="warn" type="button" (click)="confirm()">{{ 'confirm.confirm' | translate }}</button>
    </mat-dialog-actions>
  `,
  styles: [
    `
      .dialog-title {
        display: flex;
        align-items: center;
        gap: 0.65rem;
        margin: 0;
        font-size: 1.15rem;
        font-weight: 600;
      }
      .title-icon {
        color: var(--warning, #d97706);
        font-size: 1.25rem;
      }
      .dialog-content {
        padding-top: 0.25rem;
      }
      .dialog-content p {
        margin: 0;
        line-height: 1.5;
        color: var(--text-muted, #64748b);
      }
    `,
  ],
})
export class ConfirmDialogComponent {
  readonly data = inject<ConfirmDialogData>(MAT_DIALOG_DATA);
  private readonly ref = inject(MatDialogRef<ConfirmDialogComponent, boolean>);

  cancel(): void {
    this.ref.close(false);
  }

  confirm(): void {
    this.ref.close(true);
  }
}
