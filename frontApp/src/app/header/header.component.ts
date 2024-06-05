import { booleanAttribute, Component, DoCheck, OnInit } from '@angular/core'
import { IonicModule } from '@ionic/angular'
import { FormsModule } from '@angular/forms'
import { NgIf } from '@angular/common'
import { RouterLink } from '@angular/router'
import { AuthService } from '../services/auth.service'
import {
  contrastOutline,
  personOutline,
  cartOutline,
  cart,
} from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { Cart } from '../models/cart.entity'
import { CartService } from '../services/cart.service'

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [IonicModule, FormsModule, NgIf, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit, DoCheck {
  darkMode: boolean | undefined
  public isLoggedIn: boolean = false
  public cart: Cart = { lineas: [], totalPrice: 0, totalQuantity: 0 }

  constructor(
    protected authService: AuthService,
    private cartService: CartService,
  ) {
    this.checkLoggedIn()
    this.cartService.cart$.subscribe((cart) => {
      this.cart = cart
    })
  }

  ngOnInit(): void {
    addIcons({ contrastOutline, personOutline, cartOutline })

    let mode = localStorage.getItem('darkMode')

    if (mode === 'false' || mode === 'true') {
      this.darkMode = booleanAttribute(mode)
    } else {
      this.darkMode = window.matchMedia('(prefers-color-scheme: dark)').matches
    }

    this.initMode()
  }

  ngDoCheck(): void {
    this.checkLoggedIn()
  }

  protected initMode(): void {
    if (this.darkMode) this.setMode()
  }

  protected toggleDarkMode(): void {
    this.darkMode = !this.darkMode
    this.setMode()
    localStorage.setItem('darkMode', String(this.darkMode))
  }

  protected setMode() {
    document.body.classList.toggle('dark')
  }

  protected logout() {
    this.authService.logout()
  }

  protected checkLoggedIn() {
    this.isLoggedIn = this.authService.isLoggedIn()
  }

  protected readonly RouterLink = RouterLink
}
