import { Component, OnInit } from '@angular/core'
import { NavigationExtras, Router, RouterLink } from '@angular/router'
import { AuthService } from '../../services/auth.service'
import { AppComponent } from '../../app.component'
import { IonicModule } from '@ionic/angular'
import { CurrencyPipe, DatePipe, NgForOf, NgIf } from '@angular/common'
import { UserResponseDto } from '../../models/user.entity'
import { UserService } from '../../services/user.service'
import { addIcons } from 'ionicons'
import { starOutline, starSharp } from 'ionicons/icons'
import { PaginatePipe } from '../../pipes/paginate.pipe'

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
  ) {
    addIcons({ starOutline, starSharp })
  }

  ngOnInit(): void {
    this.userService.meDetails().subscribe((data) => {
      this.user = data
    })
    if (!this.user) throw new Error('Prueba más tarde')
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

  protected readonly Math = Math
}
