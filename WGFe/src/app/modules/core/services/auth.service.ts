import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import {
  AuthResponse,
  IUser,
  LoginData,
  RegisterData,
} from '../models/auth.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  apiUrl = `${environment.apiUrl}/auth`;
  constructor(private http: HttpClient) {}

  login(body: LoginData): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/login`, body, {
      withCredentials: true,
    });
  }

  logout(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/logout`, {
      withCredentials: true,
    });
  }

  autoLogin(): Observable<IUser> {
    return this.http.get<IUser>(`${this.apiUrl}/auto-login`, {
      withCredentials: true,
    });
  }

  register(body: RegisterData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, body);
  }
}
