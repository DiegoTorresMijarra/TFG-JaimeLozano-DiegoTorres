import {Component, inject, OnInit} from '@angular/core'
import { CommonModule } from '@angular/common'
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms'
import { IonicModule } from '@ionic/angular'
import { Order, OrderState } from '../../../models/order.entity'
import { ActivatedRoute, Router, RouterLink } from '@angular/router'
import { OrderService } from '../../../services/orders.service'
import { ProductService } from '../../../services/product.service'
import { OrderedProduct } from '../../../models/orderedProduct.entity'
import { getProductUrl, Product } from '../../../models/product.entity'
import {AddressesService} from "../../../services/addresses.service";
import {UserService} from "../../../services/user.service";
import {RestaurantService} from "../../../services/restaurant.service";

@Component({
  selector: 'app-details',
  templateUrl: './details.page.html',
  styleUrls: ['./details.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterLink,
    ReactiveFormsModule,
  ],
})
export class DetailsPage implements OnInit {
  order: Order | null = null
  orderForm: FormGroup
  isToastOpen = false
  public restaurantNames: string | undefined
  public userNames: string | undefined
  public addresesNames: string | undefined
  private addressService = inject(AddressesService)
  private userService = inject(UserService)
  private restaurantService = inject(RestaurantService)

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private router: Router,
    private productService: ProductService,
    private fb: FormBuilder,
  ) {
    this.orderForm = this.fb.group({
      state: [''],
      isPaid: [''],
    })
  }

  ngOnInit(): void {
    const orderId = this.route.snapshot.paramMap.get('id')
    if (orderId) {
      this.getOrderDetails(orderId)
    }
  }

  getOrderDetails(orderId: string): void {
    this.orderService.getOrder(orderId).subscribe((data: Order) => {
      this.order = data
      if (!this.order) {
        console.error('Error fetching order details')
        this.router.navigate(['/'])
      }
      this.loadProductDetails()
      this.getUser(data.userId)
      this.getRestaurante(data.restaurantId)
      this.getAddress(data.addressesId)
      this.orderForm.patchValue({
        state: this.order.state,
        isPaid: this.order.isPaid,
      })
    })
  }

  loadProductDetails(): void {
    if (!this.order) throw new Error('Prueba más tarde')
    this.order.orderedProducts.forEach((orderedProduct: OrderedProduct) => {
      this.productService
        .getProduct(orderedProduct.productId.toString())
        .subscribe((product: Product) => {
          orderedProduct.product = product
          if (!orderedProduct.product) {
            console.error('Error fetching product details')
            this.router.navigate(['/'])
          }
        })
    })
  }

  getRestaurante(restaurantId: number): void {
    if (!this.restaurantNames) {
      const restaurantIdStr = restaurantId.toString()
      this.restaurantService.getRestaurant(restaurantIdStr).subscribe({
        next: (restaurant) => {
          this.restaurantNames = restaurant.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.restaurantNames = 'Error al obtener el nombre'
        },
      })
    }
  }

  getAddress(addressId: string): void {
    if (!this.addresesNames) {
      this.addressService.getAddress(addressId).subscribe({
        next: (address) => {
          this.addresesNames = address.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.addresesNames = 'Error al obtener el nombre'
        },
      })
    }
  }

  getUser(userId: string): void {
    if (!this.userNames) {
      this.userService.getUser(userId).subscribe({
        next: (user) => {
          this.userNames = user.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.userNames = 'Error al obtener el nombre'
        },
      })
    }
  }

  patchOrder() {
    if (!this.order || !this.orderForm)
      throw new Error('No se puede actualizar el pedido')

    const updatedState = this.orderForm.value.state
    const updatedIsPaid = this.orderForm.value.isPaid

    const stateChanged = updatedState !== this.order.state
    const isPaidChanged = updatedIsPaid !== this.order.isPaid

    if (!stateChanged && !isPaidChanged) {
      console.error('No se ha realizado ningún cambio en el pedido.')
      return // No hay cambios, salir de la función
    }

    this.orderService
      .updateIsPaidById(this.order.id!, updatedIsPaid)
      .subscribe({
        next: (response) => {
          console.log('Orden actualizada:', response)
        },
        error: (error) => {
          console.error('Error actualizando la orden:', error)
        },
      })

    this.orderService.patchStateById(this.order.id!, updatedState).subscribe({
      next: (response) => {
        console.log('Orden actualizada:', response)
      },
      error: (error) => {
        console.error('Error actualizando la orden:', error)
      },
    })

    this.isToastOpen = true
    setTimeout(() => {
      this.isToastOpen = false
      this.router.navigate([this.router.url]) // Navega a la misma página para recargar la vista
    }, 2000)

    this.router.navigate([])
  }
  protected readonly getProductUrl = getProductUrl
  protected readonly OrderState = OrderState
  protected readonly Object = Object
}
