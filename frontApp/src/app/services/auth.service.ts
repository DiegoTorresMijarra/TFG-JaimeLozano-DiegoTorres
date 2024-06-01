import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, map} from "rxjs";
import {Router} from "@angular/router";
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'https://localhost:3000/v1/auth';
  private tokenKey = 'authToken';

  constructor(private router: Router, private http: HttpClient) { }

  login( username: string, password: string): Observable<string> {
    const url = `${this.apiUrl}/signin`;
    const body = { username, password };

    return this.http.post(url, body, { responseType: 'json' }).pipe(
      map((response: any) => {
        // Verificar si hay un token en la respuesta JSON
        const token = response.token;

        // Si se encuentra un token, almacenarlo en el almacenamiento local
        if (token) {
          localStorage.setItem(this.tokenKey, token);
        }
        // Devolver el token (puede ser nulo si no se encontró un token en la respuesta)
        return token;
      })
    );
  }

  register(user: UserSignUpRequest): Observable<any> {
    const url = `${this.apiUrl}/signup`;
    return this.http.post(url, user, { responseType: 'json' }).pipe(
      map((response: any) => {
        return response;
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getUserName(): string | null {
    const token = this.getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.sub;
    }
    return null;
  }

  getUserRoles(): string[] | null {
    const token = this.getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      if (decodedToken.roles && typeof decodedToken.roles === 'string') {
        return decodedToken.roles.slice(1, -1).split(', ');
      }
    }
    return null;
  }

  hasRole(role: string): boolean {
    const roles = this.getUserRoles();
    if (roles === null) {
      return false;
    }
    return roles.includes(role);
  }
}
export interface UserSignUpRequest {
  name: string;
  surname: string;
  username: string;
  email: string;
  password: string;
  passwordRepeat: string;
}
