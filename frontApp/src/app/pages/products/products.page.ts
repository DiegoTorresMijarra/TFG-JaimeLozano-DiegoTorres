import { Component, inject, OnInit } from '@angular/core'
import { CommonModule, DatePipe, NgForOf, NgIf } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel,
  IonList,
  IonMenuButton,
  IonRange,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { ProductService } from '../../services/product.service'
import { AuthService } from '../../services/auth.service'
import { RouterLink } from '@angular/router'
import { EvaluationService } from '../../services/evaluation.service'
import { forkJoin } from 'rxjs'
import { addIcons } from 'ionicons'
import { starOutline, starSharp } from 'ionicons/icons'
import { Product } from '../../models/product.entity'
import { EvaluationResponseDto } from '../../models/evaluation.entity'
import { PageResponse } from '../../models/pageResponse.entity'
import { IonicModule } from '@ionic/angular'
import { ProductFiltersDto } from '../../models/productFiltersDto.entity'

@Component({
  selector: 'app-products',
  templateUrl: './products.page.html',
  styleUrls: ['./products.page.scss'],
  standalone: true,
  imports: [IonicModule, RouterLink, DatePipe, NgIf, NgForOf],
})
export class ProductsPage implements OnInit {
  public products: Product[] = []
  private productService = inject(ProductService)
  private authService = inject(AuthService)
  private evaluationService = inject(EvaluationService)
  public isAdmin: boolean = false
  public isWorker: boolean = false
  public searchName: string = ''
  filters: ProductFiltersDto = new ProductFiltersDto({
    page: 0,
    size: 2,
    direction: 'asc',
  })
  loading: boolean = false
  totalPages: number = 0

  constructor() {
    addIcons({ starOutline, starSharp })
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
      if (this.filters.page >= this.totalPages) {
        event.target.disabled = true
      }

      this.filters.page += 1 // Incrementar el número de página después de cargar los datos
    })
    console.log(this.filters)
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
