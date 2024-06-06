import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonContent,
  IonHeader,
  IonItem,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { IonicModule } from '@ionic/angular'
import { Order } from '../../../models/order.entity'
import { ActivatedRoute, Router, RouterLink } from '@angular/router'
import { OrderService } from '../../../services/orders.service'
import { ProductService } from '../../../services/product.service'
import { OrderedProduct } from '../../../models/orderedProduct.entity'
import { getProductUrl, Product } from '../../../models/product.entity'

@Component({
  selector: 'app-details',
  templateUrl: './details.page.html',
  styleUrls: ['./details.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, RouterLink],
})
export class DetailsPage implements OnInit {
  order: Order | null = null

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private router: Router,
    private productService: ProductService,
  ) {}

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

  protected readonly getProductUrl = getProductUrl
}
