import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import { Product, ProductSaveDto } from '../models/product.entity'
import { PageResponse } from '../models/pageResponse.entity'
import { ProductFiltersDto } from '../models/productFiltersDto.entity'

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'https://localhost:3000/v1/products'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getProductsList(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/listAll`)
  }

  getProducts(filters?: ProductFiltersDto): Observable<PageResponse<Product>> {
    return this.http.post<PageResponse<Product>>(
      `${this.apiUrl}/pageAll`,
      filters,
    )
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
    const headers = this.authService.getAuthHeaders()
    return this.http
      .post<Product>(`${this.apiUrl}/saveProduct`, product, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error guardando producto:', error)
          return throwError(error)
        }),
      )
  }

  updateProduct(id: string, product: ProductSaveDto): Observable<Product> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .put<Product>(`${this.apiUrl}/updateProduct/${id}`, product, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando producto:', error)
          return throwError(error)
        }),
      )
  }

  getProduct(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        // Manejo de errores
        console.error('Error obteniendo producto:', error)
        return throwError(error)
      }),
    )
  }

  updateProductPhoto(id: number, image: File): Observable<Product> {
    let headers = this.authService.getAuthHeaders()
    headers = headers.delete('Content-Type')

    const formData = new FormData()
    formData.append('image', image, image.name)

    return this.http
      .patch<Product>(`${this.apiUrl}/updateProductPhoto/${id}`, formData, {
        headers,
      })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando imagen del producto:', error)
          return throwError(error)
        }),
      )
  }
}
