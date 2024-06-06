import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { IonicModule } from '@ionic/angular'
import { Cart } from '../../models/cart.entity'
import { CartService } from '../../services/cart.service'
import { getProductUrl } from '../../models/product.entity'

@Component({
  selector: 'app-cart',
  templateUrl: './cart.page.html',
  styleUrls: ['./cart.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],
})
export class CartPage implements OnInit {
  cart: Cart = { lineas: [], totalPrice: 0, totalQuantity: 0 }

  constructor(private cartService: CartService) {}

  ngOnInit() {
    this.cartService.cart$.subscribe((cart) => {
      this.cart = cart
    })
  }

  removeLine(productId?: number) {
    if (productId) this.cartService.removeFromCart(productId)
  }

  protected readonly getProductUrl = getProductUrl
}
