import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import { Category, CategoryDto } from '../models/category.entity'

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiUrl = 'https://localhost:3000/v1/categories'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getCategories(): Observable<Category[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Product[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Category[]>(`${this.apiUrl}/listAll`)
  }

  deleteCategory(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .delete<void>(`${this.apiUrl}/deleteCategory/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error borrando categoria:', error)
          return throwError(error)
        }),
      )
  }

  saveCategory(category: CategoryDto): Observable<Category> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .post<Category>(`${this.apiUrl}/saveCategory`, category, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error guardando categoria:', error)
          return throwError(error)
        }),
      )
  }

  updateCategory(id: string, category: CategoryDto): Observable<Category> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .put<Category>(`${this.apiUrl}/updateCategory/${id}`, category, {
        headers,
      })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando categoria:', error)
          return throwError(error)
        }),
      )
  }

  getCategory(id: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        // Manejo de errores
        console.error('Error obteniendo categoria:', error)
        return throwError(error)
      }),
    )
  }
}
