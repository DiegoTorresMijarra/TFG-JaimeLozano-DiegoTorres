import { Component, OnInit, ViewChild } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { ProductFiltersDto } from '../../../models/productFiltersDto.entity'
import { getProductUrl, Product } from '../../../models/product.entity'
import { Category } from '../../../models/category.entity'
import { IonicModule, IonInfiniteScroll, ModalController } from '@ionic/angular'
import { CategoryService } from '../../../services/category.service'
import { ProductService } from '../../../services/product.service'
import { ProductModalComponent } from '../../products/modal/modal.component'
import { AnimationService } from '../../../services/animation.service'
import { Offer } from '../../../models/offer.entity'
import { OfferService } from '../../../services/offer.service'

@Component({
  selector: 'app-menu',
  templateUrl: './menu.page.html',
  styleUrls: ['./menu.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
})
export class MenuPage implements OnInit {
  public categories: Category[] = []
  public products: Product[] = []
  public selectedCategoryId: number | undefined = 1
  filters: ProductFiltersDto = new ProductFiltersDto({
    page: 0,
    size: 4,
    direction: 'asc',
    categoryId: this.selectedCategoryId,
  })
  loading: boolean = false
  totalPages: number = 0
  @ViewChild(IonInfiniteScroll) infiniteScroll: IonInfiniteScroll | undefined

  constructor(
    private modalController: ModalController,
    private animationService: AnimationService,
    private categoryService: CategoryService,
    private productService: ProductService,
    private offerService: OfferService,
  ) {}

  ngOnInit() {
    this.loadCategories()
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe((categories) => {
      this.categories = categories
      if (this.categories.length > 0) {
        this.selectedCategoryId = this.categories[0].id
        this.loadMoreData()
      }
    })
  }

  onCategoryChange(event: any) {
    this.selectedCategoryId = event.detail.value
    this.filters.page = 0
    this.filters.categoryId = this.selectedCategoryId
    this.products = []
    if (this.infiniteScroll) {
      this.infiniteScroll.disabled = false
    }
    this.loadMoreData()
  }

  loadMoreData(event?: any) {
    if (this.loading || !this.selectedCategoryId) return

    this.loading = true

    this.productService.getProducts(this.filters).subscribe((response) => {
      this.products = [...this.products, ...response.content]
      this.totalPages = response.totalPages
      this.loading = false

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
      if (this.infiniteScroll && this.filters.page >= this.totalPages) {
        this.infiniteScroll.disabled = true
      }

      this.filters.page += 1
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
  protected readonly getProductUrl = getProductUrl
}
