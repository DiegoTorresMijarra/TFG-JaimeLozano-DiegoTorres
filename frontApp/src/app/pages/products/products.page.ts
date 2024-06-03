import { Component, inject, OnInit, ViewChild } from '@angular/core'
import { CommonModule, DatePipe, NgForOf, NgIf } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { ProductService } from '../../services/product.service'
import { AuthService } from '../../services/auth.service'
import { RouterLink } from '@angular/router'
import { EvaluationService } from '../../services/evaluation.service'
import { forkJoin } from 'rxjs'
import { addIcons } from 'ionicons'
import { trashOutline, starOutline, starSharp } from 'ionicons/icons'
import { Product } from '../../models/product.entity'
import { EvaluationResponseDto } from '../../models/evaluation.entity'
import { PageResponse } from '../../models/pageResponse.entity'
import { IonicModule, IonInfiniteScroll } from '@ionic/angular'
import { ProductFiltersDto } from '../../models/productFiltersDto.entity'

@Component({
  selector: 'app-products',
  templateUrl: './products.page.html',
  styleUrls: ['./products.page.scss'],
  standalone: true,
  imports: [IonicModule, RouterLink, DatePipe, NgIf, NgForOf, FormsModule],
})
export class ProductsPage implements OnInit {
  public products: Product[] = []
  private productService = inject(ProductService)
  private authService = inject(AuthService)
  private evaluationService = inject(EvaluationService)
  public isAdmin: boolean = false
  public isWorker: boolean = false
  public searchName: string | undefined = undefined
  filters: ProductFiltersDto = new ProductFiltersDto({
    page: 0,
    size: 2,
    direction: 'asc',
  })
  loading: boolean = false
  totalPages: number = 0
  @ViewChild(IonInfiniteScroll) infiniteScroll: IonInfiniteScroll | undefined

  constructor() {
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

      if (event) {
        event.target.complete()
      }
      // Si no hay más datos, deshabilitar el Infinite Scroll
      if (this.infiniteScroll && this.filters.page >= this.totalPages) {
        this.infiniteScroll.disabled = true
      }

      this.filters.page += 1 // Incrementar el número de página después de cargar los datos
    })
    console.log(this.filters)
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

  protected readonly Math = Math
}
