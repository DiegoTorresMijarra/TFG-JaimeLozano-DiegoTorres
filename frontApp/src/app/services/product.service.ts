import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'https://localhost:3000/v1/products';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getProducts(): Observable<Product[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Product[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Product[]>(`${this.apiUrl}/listAll`);
  }

  deleteProduct(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders();
    return this.http.delete<void>(`${this.apiUrl}/deleteProduct/${id}`, { headers }).pipe(
      catchError(error => {
        // Manejo de errores
        console.error('Error borrando producto:', error);
        return throwError(error);
      })
    );
  }

}

export interface Product {
  id?: number;
  name: string;
  price: number;
  stock: number;
  gluten: boolean;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
  category: Category;
}
export interface Category{
  id?: number;
  name: string;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
}
