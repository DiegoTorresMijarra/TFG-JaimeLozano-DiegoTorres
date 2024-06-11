import { Component, Input, OnInit } from '@angular/core'
import { IonicModule, ModalController } from '@ionic/angular'
import { addIcons } from 'ionicons'
import { closeOutline, closeSharp } from 'ionicons/icons'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { RouterLink } from '@angular/router'
import { Order } from '../../../models/order.entity'

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, IonicModule],
})
export class OrderModalComponent {
  showProducts: boolean = false
  @Input() order: Order | undefined
  @Input() restaurantNames: { [key: number]: string } = {}
  @Input() productsNames: { [key: number]: string } = {}
  @Input() userNames: { [key: string]: string } = {}
  @Input() addresesNames: { [key: string]: string } = {}

  constructor(private modalController: ModalController) {
    addIcons({ closeOutline, closeSharp })
  }

  async dismissModal() {
    return await this.modalController.dismiss()
  }

  toggleProducts() {
    this.showProducts = !this.showProducts
  }

  getRestaurantName(restaurantId: number | undefined): string {
    if (restaurantId !== undefined) {
      return this.restaurantNames[restaurantId]
    } else {
      return 'Sin restaurante'
    }
  }

  getProductName(productId: number | undefined): string {
    if (productId !== undefined) {
      return this.productsNames[productId]
    } else {
      return 'Sin producto'
    }
  }

  getUserName(userId: string | undefined): string {
    if (userId !== undefined) {
      return this.userNames[userId]
    } else {
      return 'Sin usuario'
    }
  }

  getAddress(addresId: string | undefined): string {
    if (addresId !== undefined) {
      return this.addresesNames[addresId]
    } else {
      return 'Sin direccion'
    }
  }

}
