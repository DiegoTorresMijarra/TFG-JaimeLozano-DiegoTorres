import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import {Category} from "./category.service";
import {Evaluation, EvaluationDto} from "./evaluation.service";

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'https://localhost:3000/v1/products'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getProducts(): Observable<Product[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Product[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Product[]>(`${this.apiUrl}/listAll`)
  }

  deleteProduct(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .delete<void>(`${this.apiUrl}/deleteProduct/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error borrando producto:', error)
          return throwError(error)
        }),
      )
  }

  saveProduct(product: ProductSaveDto): Observable<Product> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .post<Product>(`${this.apiUrl}/saveProduct`, product, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error guardando producto:', error);
          return throwError(error);
        })
      );
  }

  updateProduct(id: string, product: ProductSaveDto): Observable<Product> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .put<Product>(`${this.apiUrl}/updateProduct/${id}`, product, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando producto:', error);
          return throwError(error);
        })
      );
  }

  getProduct(id: string): Observable<Product> {
    return this.http
      .get<Product>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error obteniendo producto:', error)
          return throwError(error)
        }),
      )
  }

}

export interface Product {
  id?: number
  name: string
  price: number
  stock: number
  gluten: boolean
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  category: Category
  averageRating: number
}
export interface ProductSaveDto {
  name: string;
  price: number;
  stock: number;
  gluten: boolean;
  categoryId: number;
}
