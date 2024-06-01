import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'

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
    const headers = this.authService.getAuthHeaders();
    return this.http.get<Order[]>(`${this.apiUrl}/listAll`, { headers } )
  }

  getOrder(id: string): Observable<Order> {
    const headers = this.authService.getAuthHeaders();
    return this.http
      .get<Order>(`${this.apiUrl}/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error obteniendo pedido:', error)
          return throwError(error)
        }),
      )
  }
}
export enum OrderState {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  DELIVERED = 'DELIVERED',
  CANCELED = 'CANCELED',
  DELETED = 'DELETED'
}
export interface OrderedProduct {
  quantity: number;
  productId: number;
  productPrice: number;
  totalPrice: number;
}
export interface Order {
  id?: string; // Usaremos string para ObjectId
  userId: string; // UUID como string
  restaurantId: number;
  addressesId: string; // UUID como string
  orderedProducts: OrderedProduct[];
  totalPrice: number;
  totalQuantityProducts: number;
  isPaid: boolean;
  state: OrderState;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
}
