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
import { addIcons } from 'ionicons'
import { starOutline, starSharp } from 'ionicons/icons'
import { Product } from '../../models/product.entity'
import { PageResponse } from '../../models/pageResponse.entity'
import {AnimationService} from "../../services/animation.service";
import {ModalController} from "@ionic/angular";
import {ProductModalComponent} from "./modal/modal.component";
import {OfferService} from "../../services/offer.service";
import {Offer} from "../../models/offer.entity";

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
  private offerService = inject(OfferService)
  private authService = inject(AuthService)
  private animationService = inject(AnimationService)
  private evaluationService = inject(EvaluationService)
  public isAdmin: boolean = false
  public isWorker: boolean = false
  constructor(private modalController: ModalController) {
    addIcons({ starOutline, starSharp })
  }

  ngOnInit() {
    this.loadProducts()
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN')
    this.isWorker = this.authService.hasRole('ROLE_WORKER')
  }

  async openDetailsModal(product: Product) {
    await this.presentModal(product)
  }

  loadProducts() {
    this.productService.getProducts().subscribe({
      next: (page: PageResponse<Product>) => {
        // Actualizar la lista de productos
        this.products = page.content;

        // Para cada producto, verifique si hay una oferta y si la hay, aplíquela al precio del producto
        this.products.forEach((product: Product) => {
          this.offerService.getActiveOfferByProductId(product.id).subscribe({
            next: (offer: Offer) => {
              if (offer) {
                const discountAmount = product.price * (offer.descuento / 100);
                product.priceOffer = product.price - discountAmount;
              }
            },
            error: (error) => {
              console.error('Error obteniendo oferta para el producto', product.id, error);
            }
          });
        });
      },
      error: (error) => {
        console.error('Error obteniendo productos', error);
      }
    });
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
}
