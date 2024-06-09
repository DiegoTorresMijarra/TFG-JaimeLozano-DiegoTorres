import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import { IonicModule } from '@ionic/angular'
import { Cart } from '../../models/cart.entity'
import { CartService } from '../../services/cart.service'
import { getProductUrl } from '../../models/product.entity'
import { PaginatePipe } from '../../pipes/paginate.pipe'
import { Router, RouterLink } from '@angular/router'
import { UserResponseDto } from '../../models/user.entity'
import { UserService } from '../../services/user.service'
import { OrderDto } from '../../models/order.entity'
import { OrderService } from '../../services/orders.service'
import { OrderedProduct } from '../../models/orderedProduct.entity'
import { Restaurant } from '../../models/restaurant.entity'
import { RestaurantService } from '../../services/restaurant.service'
import { MaskitoDirective } from '@maskito/angular'
import {
  MaskitoOptions,
  MaskitoElementPredicate,
  maskitoTransform,
} from '@maskito/core'

@Component({
  selector: 'app-cart',
  templateUrl: './cart.page.html',
  styleUrls: ['./cart.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    PaginatePipe,
    RouterLink,
    ReactiveFormsModule,
    MaskitoDirective,
  ],
})
export class CartPage implements OnInit {
  cart: Cart = { lineas: [], totalPrice: 0, totalQuantity: 0 }
  user: UserResponseDto | undefined
  addressSeleccted: string = ''
  formGroup: FormGroup
  restaurantes: Restaurant[] = []

  currentAddressesPage: number = 1
  pageAddressesSize: number = 3

  constructor(
    private router: Router,
    private cartService: CartService,
    private userService: UserService,
    private fb: FormBuilder,
    private orderService: OrderService,
    private restaurantService: RestaurantService,
  ) {
    this.formGroup = this.fb.group({
      cardNumber: [
        '',
        [Validators.required, Validators.pattern('^[0-9 ]{19}$')],
      ],
      restaurant: ['', Validators.required],
    })
  }

  ngOnInit() {
    this.cartService.cart$.subscribe((cart) => {
      this.cart = cart
    })

    this.userService.meDetails().subscribe((data) => {
      this.user = data
      if (!this.user) {
        console.error('Prueba más tarde')
        this.router.navigate(['/'])
      }
    })

    this.restaurantService.getRestaurants().subscribe((data) => {
      this.restaurantes = data
    })
  }

  removeLine(productId?: number) {
    if (productId) this.cartService.removeFromCart(productId)
  }

  changeAddressesPage(page: number): void {
    this.currentAddressesPage = page
  }

  seleccionarAddress(id: string) {
    this.addressSeleccted = id
  }

  onSubmit() {
    if (this.formGroup.valid && this.addressSeleccted) {
      const orderDto: OrderDto = {
        restaurantId: this.formGroup.get('restaurant')?.value,
        addressesId: this.addressSeleccted,
        orderedProducts: this.cart.lineas.map(
          (linea): OrderedProduct => ({
            productId: linea.product.id,
            quantity: linea.quantity,
            productPrice: linea.price,
            totalPrice: linea.subtotal,
          }),
        ),
      }

      this.orderService.saveOrder(orderDto).subscribe({
        next: (order) => {
          this.router.navigate(['/me'])
          this.cartService.clearCart()
        },
        error: (error) => {
          console.error('Error guardando pedido:', error)
        },
      })
    }
  }

  protected readonly getProductUrl = getProductUrl
  protected readonly Math = Math

  readonly cardMask: MaskitoOptions = {
    mask: [
      ...Array(4).fill(/\d/),
      ' ',
      ...Array(4).fill(/\d/),
      ' ',
      ...Array(4).fill(/\d/),
      ' ',
      ...Array(4).fill(/\d/),
    ],
  }

  readonly maskPredicate: MaskitoElementPredicate = async (el) =>
    (el as HTMLIonInputElement).getInputElement()
}
