import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { catchError, Observable, throwError } from 'rxjs'
import { AuthService } from './auth.service'
import { Category, CategoryDto } from '../models/category.entity'
import {Offer, OfferDto} from "../models/offer.entity";

@Injectable({
  providedIn: 'root',
})
export class OfferService {
  private apiUrl = 'https://localhost:3000/v1/offers'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getOffers(): Observable<Offer[]> {
    //const headers = this.authService.getAuthHeaders();
    //return this.http.get<Product[]>(`${this.apiUrl}/listAll`, { headers });
    return this.http.get<Offer[]>(`${this.apiUrl}/listAll`)
  }

  deleteOffer(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .delete<void>(`${this.apiUrl}/deleteOffer/${id}`, { headers })
      .pipe(
        catchError((error) => {
          // Manejo de errores
          console.error('Error borrando oferta:', error)
          return throwError(error)
        }),
      )
  }

  saveOffer(offer: OfferDto): Observable<Offer> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .post<Offer>(`${this.apiUrl}/saveOffer`, offer, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error guardando oferta:', error)
          return throwError(error)
        }),
      )
  }

  updateOffer(id: string, offer: OfferDto): Observable<Offer> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .put<Offer>(`${this.apiUrl}/updateOffer/${id}`, offer, {
        headers,
      })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando oferta:', error)
          return throwError(error)
        }),
      )
  }

  getOffer(id: string): Observable<Offer> {
    return this.http.get<Offer>(`${this.apiUrl}/${id}`).pipe(
      catchError((error) => {
        // Manejo de errores
        console.error('Error obteniendo oferta:', error)
        return throwError(error)
      }),
    )
  }

  getActiveOfferByProductId(id: string): Observable<Offer> {
    return this.http.get<Offer>(`${this.apiUrl}/active/${id}`).pipe(
      catchError((error) => {
        // Manejo de errores
        console.error('Error obteniendo oferta:', error)
        return throwError(error)
      }),
    )
  }
}
