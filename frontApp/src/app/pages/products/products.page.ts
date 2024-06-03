import { Component, inject, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
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

@Component({
  selector: 'app-products',
  templateUrl: './products.page.html',
  styleUrls: ['./products.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    CommonModule,
    FormsModule,
    IonItem,
    IonLabel,
    IonList,
    IonButton,
    IonButtons,
    IonMenuButton,
    RouterLink,
    IonRange,
    IonIcon,
  ],
})
export class ProductsPage implements OnInit {
  public products: Product[] = []
  private productService = inject(ProductService)
  private authService = inject(AuthService)
  private evaluationService = inject(EvaluationService)
  public isAdmin: boolean = false
  public isWorker: boolean = false
  constructor() {
    addIcons({ starOutline, starSharp })
  }

  ngOnInit() {
    this.loadProducts()
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN')
    this.isWorker = this.authService.hasRole('ROLE_WORKER')
  }

  loadProducts() {
    this.productService
      .getProducts()
      .subscribe((page: PageResponse<Product>) => {
        // Actualizar la lista de productos
        this.products = page.content
      })
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
