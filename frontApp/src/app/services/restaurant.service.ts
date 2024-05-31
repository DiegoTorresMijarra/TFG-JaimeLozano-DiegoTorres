import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import {catchError, Observable, throwError} from 'rxjs'
import { AuthService } from './auth.service'
import {Product, ProductSaveDto} from "./product.service";
import {Category, CategoryDto} from "./category.service";

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private apiUrl = 'https://localhost:3000/v1/restaurants'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getRestaurants(): Observable<Restaurant[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Restaurant[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Restaurant[]>(`${this.apiUrl}/listAll`)
  }

  deleteRestaurant(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .delete<void>(`${this.apiUrl}/deleteRestaurant/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error borrando restaurante:', error)
          return throwError(error)
        }),
      )
  }

  saveRestaurant(restaurant: RestaurantDto): Observable<Restaurant> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .post<Restaurant>(`${this.apiUrl}/saveRestaurant`, restaurant, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error guardando producto:', error);
          return throwError(error);
        })
      );
  }

  updateRestaurant(id: string,restaurant: RestaurantDto): Observable<Restaurant> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .put<Restaurant>(`${this.apiUrl}/updateRestaurant/${id}`, restaurant, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando restaurante:', error);
          return throwError(error);
        })
      );
  }

  getRestaurant(id: string): Observable<Restaurant> {
    return this.http
      .get<Restaurant>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error obteniendo restaurante:', error)
          return throwError(error)
        }),
      )
  }

}

export interface Restaurant {
  id?: number
  name: string
  phone: string
  address: string
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
export interface RestaurantDto {
  name: string
  phone: string
  address: string
}
