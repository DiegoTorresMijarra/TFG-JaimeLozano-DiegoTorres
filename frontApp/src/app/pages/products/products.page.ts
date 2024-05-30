import { Component, inject, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonButton, IonButtons,
  IonContent,
  IonHeader,
  IonItem,
  IonLabel,
  IonList, IonMenuButton,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { Product, ProductService } from '../../services/product.service'
import { AuthService } from '../../services/auth.service'
import {RouterLink} from "@angular/router";

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
  ],
})
export class ProductsPage implements OnInit {
  public products: Product[] = []
  private productService = inject(ProductService)
  private authService = inject(AuthService)
  public isAdmin: boolean = false
  constructor() {}

  ngOnInit() {
    this.loadProducts()
    this.isAdmin = this.authService.getUserRole() === 'admin'
  }

  loadProducts() {
    this.productService.getProducts().subscribe((data) => {
      this.products = data
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
}
