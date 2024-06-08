import { Component, inject, Input, OnInit } from '@angular/core'
import { IonicModule, ModalController } from '@ionic/angular'
import { addIcons } from 'ionicons'
import {
  closeOutline,
  closeSharp,
  starOutline,
  starSharp,
} from 'ionicons/icons'
import { Product } from '../../../models/product.entity'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { RouterLink } from '@angular/router'
import { EvaluationResponseDto } from '../../../models/evaluation.entity'
import { EvaluationService } from '../../../services/evaluation.service'
import { CartService } from '../../../services/cart.service'

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, IonicModule],
})
export class ProductModalComponent implements OnInit {
  public evaluations: EvaluationResponseDto[] = []
  private evaluationService = inject(EvaluationService)
  quantity: number = 1
  cartQuantity: number = 0
  @Input() product: Product | undefined

  constructor(
    private modalController: ModalController,
    private cartService: CartService,
  ) {
    addIcons({ closeOutline, closeSharp, starOutline, starSharp })
    this.cartQuantity =
      this.cartService
        .getCart()
        .lineas.find((line) => line.product.id === this.product?.id)
        ?.quantity ?? 0
  }

  ngOnInit() {
    this.loadEvaluations(this.product?.id)
  }

  async dismissModal() {
    return await this.modalController.dismiss()
  }

  loadEvaluations(id: number | undefined) {
    this.evaluationService.getEvaluationsByProductId(id).subscribe((data) => {
      this.evaluations = data
    })
  }

  increaseQuantity() {
    if (!this.product) throw new Error()

    const maxQuantity = this.product.stock - this.cartQuantity
    if (this.quantity < maxQuantity) {
      this.quantity += 1
    }
  }

  decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity -= 1
    }
  }

  addToCart() {
    if (!this.product) throw new Error()
    this.cartService.addToCart(this.product, this.product.price, this.quantity)
    this.dismissModal()
  }
}
