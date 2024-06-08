import { Component, inject, OnInit, ViewChild } from '@angular/core'
import {
  CommonModule,
  CurrencyPipe,
  DatePipe,
  NgForOf,
  NgIf,
} from '@angular/common'
import { FormsModule } from '@angular/forms'
import { ProductService } from '../../services/product.service'
import { AuthService } from '../../services/auth.service'
import { RouterLink } from '@angular/router'
import { EvaluationService } from '../../services/evaluation.service'
import { addIcons } from 'ionicons'
import { trashOutline, starOutline, starSharp } from 'ionicons/icons'
import { Product, getProductUrl } from '../../models/product.entity'
import { PageResponse } from '../../models/pageResponse.entity'
import { IonicModule, IonInfiniteScroll, ModalController } from '@ionic/angular'
import { ProductFiltersDto } from '../../models/productFiltersDto.entity'
import { AnimationService } from '../../services/animation.service'
import { ProductModalComponent } from './modal/modal.component'
import { OfferService } from '../../services/offer.service'
import { Offer } from '../../models/offer.entity'
import { Order } from '../../models/order.entity'

@Component({
  selector: 'app-products',
  templateUrl: './products.page.html',
  styleUrls: ['./products.page.scss'],
  standalone: true,
  imports: [
    IonicModule,
    RouterLink,
    DatePipe,
    NgIf,
    NgForOf,
    FormsModule,
    CurrencyPipe,
  ],
})
export class ProductsPage implements OnInit {
  public products: Product[] = []
  private productService = inject(ProductService)
  private offerService = inject(OfferService)
  private authService = inject(AuthService)
  private animationService = inject(AnimationService)
  private evaluationService = inject(EvaluationService)
  public isAdmin: boolean = false
  public isWorker: boolean = false
  public searchName: string | undefined = undefined
  filters: ProductFiltersDto = new ProductFiltersDto({
    page: 0,
    size: 4,
    direction: 'asc',
  })
  loading: boolean = false
  totalPages: number = 0
  @ViewChild(IonInfiniteScroll) infiniteScroll: IonInfiniteScroll | undefined

  constructor(private modalController: ModalController) {
    addIcons({ trashOutline, starOutline, starSharp })
  }

  ngOnInit() {
    this.loadMoreData()
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN')
    this.isWorker = this.authService.hasRole('ROLE_WORKER')
  }

  loadMoreData(event?: any) {
    if (this.loading) return

    this.loading = true

    this.productService.getProducts(this.filters).subscribe((response) => {
      this.products = [...this.products, ...response.content]
      this.totalPages = response.totalPages
      this.loading = false

      // Para cada producto, verifique si hay una oferta y si la hay, aplíquela al precio del producto
      this.products.forEach((product: Product) => {
        this.offerService.getActiveOfferByProductId(product.id).subscribe({
          next: (offer: Offer) => {
            if (offer) {
              const discountAmount = product.price * (offer.descuento / 100)
              product.priceOffer = product.price - discountAmount
            }
          },
        })
      })

      if (event) {
        event.target.complete()
      }
      // Si no hay más datos, deshabilitar el Infinite Scroll
      if (this.infiniteScroll && this.filters.page >= this.totalPages) {
        this.infiniteScroll.disabled = true
      }

      this.filters.page += 1 // Incrementar el número de página después de cargar los datos
    })
  }

  applyFilters() {
    this.filters.page = 0
    this.filters.name = this.searchName == '' ? undefined : this.searchName
    this.products = []
    if (this.infiniteScroll) {
      this.infiniteScroll.disabled = false // Enable infinite scroll
    }
    this.loadMoreData()
  }

  deleteProduct(id: number | undefined): void {
    this.productService.deleteProduct(String(id)).subscribe({
      next: () => {
        console.log('Product deleted successfully')
        // Elimina el producto de la lista después de eliminarlo
        this.products = this.products.filter(
          (product) => product.id !== Number(id),
        )
      },
      error: (error) => {
        console.error('Error deleting product:', error)
        // Manejar el error según sea necesario
      },
    })
  }

  async openDetailsModal(product: Product) {
    await this.presentModal(product)
  }

  async presentModal(product: Product) {
    const modal = await this.modalController.create({
      component: ProductModalComponent,
      componentProps: {
        product: product,
      },
      enterAnimation: this.animationService.enterAnimation,
      leaveAnimation: this.animationService.leaveAnimation,
    })
    return await modal.present()
  }

  protected readonly Math = Math
  protected readonly getProductUrl = getProductUrl
}
