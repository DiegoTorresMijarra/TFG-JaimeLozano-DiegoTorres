import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private apiUrl = 'https://localhost:3000/v1/restaurants';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getRestaurants(): Observable<Restaurant[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Restaurant[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Restaurant[]>(`${this.apiUrl}/listAll`);
  }
}

export interface Restaurant {
  id?: number;
  name: string;
  phone: string;
  address: string;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
}
