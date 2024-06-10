import { Component, OnInit } from '@angular/core'
import { NavigationExtras, Router, RouterLink } from '@angular/router'
import { AuthService } from '../../services/auth.service'
import { AppComponent } from '../../app.component'
import { IonicModule } from '@ionic/angular'
import { CurrencyPipe, DatePipe, NgForOf, NgIf } from '@angular/common'
import { UserResponseDto } from '../../models/user.entity'
import { UserService } from '../../services/user.service'
import { addIcons } from 'ionicons'
import { starOutline, starSharp, downloadOutline } from 'ionicons/icons'
import { PaginatePipe } from '../../pipes/paginate.pipe'
import { AddressesService } from '../../services/addresses.service'
import { OrderService } from '../../services/orders.service'
import { catchError } from 'rxjs'

@Component({
  selector: 'app-login',
  templateUrl: './me.page.html',
  styleUrls: ['./me.page.scss'],
  standalone: true,
  imports: [
    IonicModule,
    RouterLink,
    NgIf,
    CurrencyPipe,
    DatePipe,
    NgForOf,
    PaginatePipe,
  ],
})
export class MePage implements OnInit {
  user: UserResponseDto | undefined

  currentOrderPage: number = 1
  currentAddressesPage: number = 1
  pageOrderSize: number = 5
  pageAddressesSize: number = 3

  constructor(
    private router: Router,
    protected authService: AuthService,
    private appComponent: AppComponent,
    private userService: UserService,
    private addressService: AddressesService,
    private orderService: OrderService,
  ) {
    addIcons({ starOutline, starSharp, downloadOutline })
  }

  ngOnInit(): void {
    this.userService.meDetails().subscribe((data) => {
      this.user = data
      if (!this.user) {
        console.error('Prueba más tarde')
        this.router.navigate(['/'])
      }
    })
  }

  goToEvaluationPage(order: any) {
    const navigationExtras: NavigationExtras = {
      state: {
        order,
      },
    }
    this.router.navigate(['/updateEvaluations'], navigationExtras)
  }

  changeOrderPage(page: number): void {
    this.currentOrderPage = page
  }

  changeAddressesPage(page: number): void {
    this.currentAddressesPage = page
  }

  deleteAddress(id: string | undefined): void {
    this.addressService.deleteAddress(String(id)).subscribe({
      next: () => {
        if (!this.user?.addresses) throw new Error()
        console.log('Categoria borrado correctamente')
        this.user.addresses = this.user?.addresses.filter(
          (address) => address.id !== id,
        )
      },
      error: (error) => {
        console.error('Error deleting product:', error)
      },
    })
  }

  downloadExcel(orderId: string): void {
    this.orderService.downloadExcel(orderId).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `Order_${orderId}.xlsx`
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        window.URL.revokeObjectURL(url)
      },
      error: (error: any) => {
        console.error('Error downloading the file', error)
      },
      complete: () => {
        console.log('File download completed')
      },
    })
  }

  protected readonly Math = Math
}
