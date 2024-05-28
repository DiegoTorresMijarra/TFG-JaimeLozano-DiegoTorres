import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {
  private apiUrl = 'https://localhost:3000/v1/restaurants';

  constructor(private http: HttpClient) { }

  getRestaurants(): Observable<Restaurant[]> {
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
