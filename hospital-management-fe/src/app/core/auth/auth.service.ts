import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { SKIP_API_FEEDBACK } from '../tokens/api-feedback.tokens';
import type { LoginRequest } from './models/request/login-request.dto';
import type { RegisterRequest } from './models/request/register-request.dto';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'hospital_token';
  private readonly userKey = 'hospital_user';

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

  login(username: string, password: string): Observable<void> {
    const normalized = username.trim();
    const body: LoginRequest = { userName: normalized, password };
    return this.http
      .post('/api/user/login', body, {
        responseType: 'text',
        context: new HttpContext().set(SKIP_API_FEEDBACK, true),
      })
      .pipe(
        tap((token) => {
          sessionStorage.setItem(this.tokenKey, token);
          sessionStorage.setItem(this.userKey, normalized);
        }),
        map(() => void 0),
      );
  }

  register(username: string, password: string): Observable<void> {
    const normalized = username.trim();
    const body: RegisterRequest = { userName: normalized, password };
    return this.http
      .post('/api/user/register', body, {
        context: new HttpContext().set(SKIP_API_FEEDBACK, true),
      })
      .pipe(map(() => void 0));
  }

  logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.userKey);
  }
}
