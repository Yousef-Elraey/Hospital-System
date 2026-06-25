import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const BASE = '/api';

@Injectable({ providedIn: 'root' })
export class ApiHttpService {
  constructor(private http: HttpClient) {}

  request<T>(
    method: string,
    path: string,
    body?: unknown,
    query?: object
  ): Observable<T> {
    const url = `${BASE}${path}`;
    const options = body != null ? { body } : {};
    let params = new HttpParams();
    Object.entries(query ?? {}).forEach(([k, v]) => {
      if (v === undefined || v === null || v === '') return;
      params = params.set(k, String(v));
    });
    return this.http.request<T>(method, url, {
      ...options,
      params,
      responseType: 'json' as 'json',
      headers: { 'Content-Type': 'application/json' },
    });
  }
}
