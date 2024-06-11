import { Injectable } from '@angular/core'
import { catchError, Observable, throwError } from 'rxjs'
import { Address, AddressDto } from '../models/address.entity'
import { HttpClient } from '@angular/common/http'
import { AuthService } from './auth.service'

@Injectable({
  providedIn: 'root',
})
export class AddressesService {
  private apiUrl = 'https://localhost:3000/v1/addresses'

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getAddresses(): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.apiUrl}/listAll`).pipe(
      catchError((error) => {
        console.error('Error obteniendo direcciones:', error)
        return throwError(error)
      }),
    )
  }

  deleteAddress(id: string): Observable<void> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .delete<void>(`${this.apiUrl}/deleteAddress/${id}`, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error borrando dirección:', error)
          return throwError(error)
        }),
      )
  }

  saveAddress(address: AddressDto): Observable<Address> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .post<Address>(`${this.apiUrl}/saveAddress`, address, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error guardando dirección:', error)
          return throwError(error)
        }),
      )
  }

  updateAddress(id: string, address: AddressDto): Observable<Address> {
    const headers = this.authService.getAuthHeaders()
    return this.http
      .put<Address>(`${this.apiUrl}/updateAddress/${id}`, address, { headers })
      .pipe(
        catchError((error) => {
          console.error('Error actualizando dirección:', error)
          return throwError(error)
        }),
      )
  }

  getAddress(id: string): Observable<Address> {
    const headers = this.authService.getAuthHeaders()

    return this.http.get<Address>(`${this.apiUrl}/${id}`, { headers }).pipe(
      catchError((error) => {
        console.error('Error obteniendo dirección:', error)
        return throwError(error)
      }),
    )
  }
}
