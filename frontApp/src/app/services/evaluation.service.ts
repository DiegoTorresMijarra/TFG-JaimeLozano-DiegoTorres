import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import {Product} from "./product.service";
import {Restaurant, RestaurantDto} from "./restaurant.service";

@Injectable({
  providedIn: 'root',
})
export class EvaluationService {
  private apiUrl = 'https://localhost:3000/v1/evaluations'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getEvaluations(): Observable<Evaluation[]> {
    const headers = this.authService.getAuthHeaders();
    return this.http.get<Evaluation[]>(`${this.apiUrl}/listAll`, { headers })
  }

  getEvaluationsByProductId(id: number | undefined): Observable<Evaluation[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Product[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Evaluation[]>(`${this.apiUrl}/listAll/${id}`)
  }

  deleteEvaluation(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .delete<void>(`${this.apiUrl}/deleteEvaluation/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error borrando valoracion:', error)
          return throwError(error)
        }),
      )
  }

  saveEvaluation(evaluation: EvaluationDto): Observable<Evaluation> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .post<Evaluation>(`${this.apiUrl}/saveEvaluation`, evaluation, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error guardando evaluation:', error);
          return throwError(error);
        })
      );
  }

  updateEvaluation(id: string,evaluation: EvaluationDto): Observable<Evaluation> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .put<Evaluation>(`${this.apiUrl}/updateEvaluation/${id}`, evaluation, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando valoracion:', error);
          return throwError(error);
        })
      );
  }

  getEvaluation(id: string): Observable<Evaluation> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .get<Evaluation>(`${this.apiUrl}/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error obteniendo valoracion:', error)
          return throwError(error)
        }),
      )
  }

}
export interface Evaluation {
  id?: number
  value: number
  comment: string
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  product: Product
}
export interface EvaluationDto{
  value: number
  comment: string
  productId: number
}
