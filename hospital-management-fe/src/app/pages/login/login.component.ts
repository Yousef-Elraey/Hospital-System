import { Component } from '@angular/core';
import { NgClass } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../core/auth/auth.service';
import { LocaleService } from '../../core/services/locale.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, TranslateModule, NgClass, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  submitting = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private translate: TranslateService,
    public locale: LocaleService
  ) {}

  onSubmit(): void {
    this.error = '';
    if (!this.username.trim()) {
      this.error = this.translate.instant('login.errorRequired');
      return;
    }
    this.submitting = true;
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/'], { replaceUrl: true }),
      error: () => {
        this.submitting = false;
        this.error = this.translate.instant('login.errorInvalid');
      },
    });
  }
}
