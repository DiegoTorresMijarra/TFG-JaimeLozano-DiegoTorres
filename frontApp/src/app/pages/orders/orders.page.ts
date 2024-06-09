import { Component, inject, OnInit } from '@angular/core'
import { CommonModule, DatePipe, NgForOf } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { OrderService } from '../../services/orders.service'
import { IonicModule, ModalController } from '@ionic/angular'
import { AnimationService } from '../../services/animation.service'
import { OrderModalComponent } from './modal/modal.component'
import { RestaurantService } from '../../services/restaurant.service'
import { ProductService } from '../../services/product.service'
import { Order } from '../../models/order.entity'
import { RouterLink } from '@angular/router'
import { PaginatePipe } from '../../pipes/paginate.pipe'
import {UserService} from "../../services/user.service";
import {AddressesService} from "../../services/addresses.service";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.page.html',
  styleUrls: ['./orders.page.scss'],
  standalone: true,
  imports: [IonicModule, RouterLink, DatePipe, NgForOf, PaginatePipe],
})
export class OrdersPage implements OnInit {
  public orders: Order[] = []
  public restaurantNames: { [key: number]: string } = {}
  public productsNames: { [key: number]: string } = {}
  public userNames: { [key: string]: string } = {}
  public addresesNames: { [key: string]: string } = {}
  private addressService = inject(AddressesService)
  private userService = inject(UserService)
  private productService = inject(ProductService)
  private restaurantService = inject(RestaurantService)
  private orderService = inject(OrderService)
  private animationService = inject(AnimationService)

  currentOrderPage: number = 1
  pageOrderSize: number = 4

  constructor(private modalController: ModalController) {}

  ngOnInit() {
    this.loadOrders()
  }
  loadOrders() {
    this.orderService.getOrders().subscribe((data) => {
      this.orders = data
      this.orders.forEach((order) => this.getRestaurante(order.restaurantId))
      this.orders.forEach((order) => this.getAddress(order.addressesId))
      this.orders.forEach((order) => this.getUser(order.userId))
      this.orders.forEach((order) =>
        order.orderedProducts.forEach((product) =>
          this.getProduct(product.productId),
        ),
      )
    })
  }

  async openDetailsModal(order: Order) {
    await this.presentModal(order)
  }

  async presentModal(order: Order) {
    const modal = await this.modalController.create({
      component: OrderModalComponent,
      componentProps: {
        order: order,
        restaurantNames: this.restaurantNames,
        productsNames: this.productsNames,
        userNames: this.userNames,
        addresesNames: this.addresesNames,
      },
      enterAnimation: this.animationService.enterAnimation,
      leaveAnimation: this.animationService.leaveAnimation,
    })
    return await modal.present()
  }

  getRestaurante(restaurantId: number): void {
    if (!this.restaurantNames[restaurantId]) {
      const restaurantIdStr = restaurantId.toString()
      this.restaurantService.getRestaurant(restaurantIdStr).subscribe({
        next: (restaurant) => {
          this.restaurantNames[restaurantId] = restaurant.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.restaurantNames[restaurantId] = 'Error al obtener el nombre'
        },
      })
    }
  }

  getAddress(addressId: string): void {
    if (!this.addresesNames[addressId]) {
      this.addressService.getAddress(addressId).subscribe({
        next: (address) => {
          this.addresesNames[addressId] = address.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.addresesNames[addressId] = 'Error al obtener el nombre'
        },
      })
    }
  }

  getUser(userId: string): void {
    if (!this.userNames[userId]) {
      this.userService.getUser(userId).subscribe({
        next: (user) => {
          this.userNames[userId] = user.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.userNames[userId] = 'Error al obtener el nombre'
        },
      })
    }
  }

  getProduct(productId: number): void {
    if (!this.productsNames[productId]) {
      const productIdStr = productId.toString()
      this.productService.getProduct(productIdStr).subscribe({
        next: (product) => {
          this.productsNames[productId] = product.name
        },
        error: (err) => {
          console.error('Error:', err)
          this.productsNames[productId] = 'Error al obtener el nombre'
        },
      })
    }
  }

  getProductName(productId: number): string {
    return this.productsNames[productId]
  }

  getRestaurantName(restaurantId: number): string {
    return this.restaurantNames[restaurantId]
  }

  getAddressName(addressId: string): string {
    return this.addresesNames[addressId]
  }

  getUserName(userId: string): string {
    return this.userNames[userId]
  }

  changeOrderPage(page: number): void {
    this.currentOrderPage = page
  }

  protected readonly Math = Math
}
