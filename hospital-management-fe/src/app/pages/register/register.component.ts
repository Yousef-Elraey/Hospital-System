import { Component } from '@angular/core';
import { NgClass } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../core/auth/auth.service';
import { LocaleService } from '../../core/services/locale.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, TranslateModule, NgClass, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  username = '';
  password = '';
  confirmPassword = '';
  error = '';
  submitting = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private translate: TranslateService,
    public locale: LocaleService,
  ) {}

  onSubmit(): void {
    this.error = '';
    if (!this.username.trim()) {
      this.error = this.translate.instant('register.errorRequiredUsername');
      return;
    }
    if (!this.password) {
      this.error = this.translate.instant('register.errorRequiredPassword');
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.error = this.translate.instant('register.errorPasswordMismatch');
      return;
    }

    this.submitting = true;
    this.auth.register(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/login'], { replaceUrl: true }),
      error: () => {
        this.submitting = false;
        this.error = this.translate.instant('register.errorFailed');
      },
    });
  }
}

