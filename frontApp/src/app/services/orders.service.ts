import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import { Order } from '../models/order.entity'

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = 'https://localhost:3000/v1/orders'

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
}
