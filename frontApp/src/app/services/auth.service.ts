import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, map} from "rxjs";
import {Router} from "@angular/router";

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

}
