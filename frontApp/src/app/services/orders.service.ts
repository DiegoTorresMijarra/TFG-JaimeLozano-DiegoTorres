import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import { Order, OrderDto } from '../models/order.entity'
import { environment } from '../../environments/environment'
import { environment as envProd } from '../../environments/environment.prod'

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl =
    (environment.production ? envProd.apiUrl : environment.apiUrl) + 'orders'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getOrders(): Observable<Order[]> {
    const headers = this.authService.getAuthHeaders()
    return this.http.get<Order[]>(`${this.apiUrl}/listAll`, { headers })
  }

  getOrder(id: string): Observable<Order> {
    const headers = this.authService.getAuthHeaders()
    return this.http.get<Order>(`${this.apiUrl}/${id}`, { headers }).pipe(
      catchError((error) => {
        // Manejo de errores
        console.error('Error obteniendo pedido:', error)
        return throwError(error)
      }),
    )
  }

  updateIsPaidById(id: string, isPaid: boolean): Observable<Order> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .patch<Order>(
        `${this.apiUrl}/updateIsPaid/${id}?isPaid=${isPaid}`,
        {},
        { headers },
      )
      .pipe(
        catchError((error) => {
          console.error('Error obteniendo pedido:', error)
          return throwError(error)
        }),
      )
  }

  patchStateById(id: string, state: string): Observable<Order> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .patch<Order>(
        `${this.apiUrl}/patchState/${id}?state=${state}`,
        {},
        { headers },
      )
      .pipe(
        catchError((error) => {
          console.error('Error obteniendo pedido:', error)
          return throwError(error)
        }),
      )
  }
  saveOrder(orderDto: OrderDto): Observable<Order> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .post<Order>(`${this.apiUrl}/saveOrder`, orderDto, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error guardando pedido:', error)
          return throwError(error)
        }),
      )
  }

  downloadExcel(orderId: string): Observable<Blob> {
    const headers = this.authService.getAuthHeaders()
    const url = `${this.apiUrl}/getExcelById/${orderId}`
    return this.http.get(url, { headers, responseType: 'blob' }).pipe(
      catchError((error) => {
        console.error('Error descargando el archivo Excel:', error)
        return throwError(error)
      }),
    )
  }
}
