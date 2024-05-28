import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'https://localhost:3000/v1/products';

  constructor(private http: HttpClient) { }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/listAll`);
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
