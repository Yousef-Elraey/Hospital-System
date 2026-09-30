import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { SKIP_API_FEEDBACK } from '../tokens/api-feedback.tokens';
import type { LoginRequest } from './models/request/login-request.dto';
import type { LoginResponse } from './models/response/login-response.dto';
import type { RegisterRequest } from './models/request/register-request.dto';

export interface RegisterFormData {
  userName: string;
  phone: string;
  email: string;
  password: string;
  fullName: string;
  address: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'hospital_token';
  private readonly userKey = 'hospital_user';
  private readonly expiryKey = 'hospital_token_expiry';

  constructor(private http: HttpClient) {}

  get isLoggedIn(): boolean {
    return !!sessionStorage.getItem(this.tokenKey);
  }

  get token(): string | null {
    return sessionStorage.getItem(this.tokenKey);
  }

  get username(): string | null {
    return sessionStorage.getItem(this.userKey);
  }

  login(email: string, password: string): Observable<void> {
    const normalized = email.trim();
    const body: LoginRequest = { email: normalized, password };
    return this.http
      .post<LoginResponse>('/api/auth/login', body, {
        context: new HttpContext().set(SKIP_API_FEEDBACK, true),
      })
      .pipe(
        tap((res) => {
          sessionStorage.setItem(this.tokenKey, res.token);
          sessionStorage.setItem(this.userKey, normalized);
          sessionStorage.setItem(this.expiryKey, res.expiresIn);
        }),
        map(() => void 0),
      );
  }

  register(data: RegisterFormData): Observable<void> {
    const body: RegisterRequest = {
      userName: data.userName.trim(),
      phone: data.phone.trim(),
      email: data.email.trim(),
      password: data.password,
      fullName: data.fullName.trim(),
      address: data.address.trim(),
      // Defaults for fields not exposed on the public signup form.
      // TODO: confirm these against your backend's expected values/enums.
      summary: '',
      status: 'ACTIVE',
      role: 'PATIENT',
      active: true,
    };
    return this.http
      .post('/api/auth/register', body, {
        context: new HttpContext().set(SKIP_API_FEEDBACK, true),
      })
      .pipe(map(() => void 0));
  }

  logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.userKey);
    sessionStorage.removeItem(this.expiryKey);
  }
}
