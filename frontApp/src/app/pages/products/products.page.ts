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
import { Product, ProductService } from '../../services/product.service'
import { AuthService } from '../../services/auth.service'
import { RouterLink } from '@angular/router'
import {
  Evaluation, EvaluationResponseDto,
  EvaluationService,
} from '../../services/evaluation.service'
import { forkJoin } from 'rxjs'
import { addIcons } from 'ionicons'
import { starOutline, starSharp } from 'ionicons/icons'

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
  constructor() {
    addIcons({ starOutline, starSharp })
  }

  ngOnInit() {
    this.loadProducts()
    this.isAdmin = this.authService.hasRole("ROLE_ADMIN")
  }

  loadProducts() {
    this.productService.getProducts().subscribe((products: Product[]) => {
      // Obtener las evaluaciones de cada producto
      const evaluationsObservables = products.map((product: Product) =>
        this.evaluationService.getEvaluationsByProductId(product.id),
      )

      // Combinar las observables de las evaluaciones
      forkJoin(evaluationsObservables).subscribe(
        (evaluationsLists: EvaluationResponseDto[][]) => {
          // Calcular la media de las evaluaciones para cada producto
          for (let i = 0; i < products.length; i++) {
            const evaluations = evaluationsLists[i]
            if (evaluations.length > 0) {
              products[i].averageRating =
                evaluations.reduce((acc, curr) => acc + curr.value, 0) /
                evaluations.length
            } else {
              products[i].averageRating = 5
            }
          }
          // Actualizar la lista de productos
          this.products = products
        },
      )
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
