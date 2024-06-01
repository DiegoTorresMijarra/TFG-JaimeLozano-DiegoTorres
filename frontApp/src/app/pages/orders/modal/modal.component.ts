import {Component, Input, OnInit} from '@angular/core';
import {Order} from "../../../services/orders.service";
import {ModalController} from "@ionic/angular";
import {addIcons} from "ionicons";
import {closeOutline, closeSharp} from "ionicons/icons";
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader, IonIcon,
  IonItem, IonItemDivider,
  IonLabel, IonList, IonText,
  IonTitle,
  IonToolbar
} from "@ionic/angular/standalone";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonButtons, IonItem, IonLabel, IonList, RouterLink, IonIcon, IonText, IonItemDivider]
})
export class OrderModalComponent {
  showProducts: boolean = false;
  @Input() order: Order | undefined;
  @Input() restaurantNames: { [key: number]: string } = {};
  @Input() productsNames: { [key: number]: string } = {};

  constructor(private modalController: ModalController) {
    addIcons({closeOutline , closeSharp })
  }

  async dismissModal() {
    return await this.modalController.dismiss();
  }

  toggleProducts() {
    this.showProducts = !this.showProducts;
  }

  getRestaurantName(restaurantId: number | undefined): string {
    if (restaurantId !== undefined) {
      return this.restaurantNames[restaurantId];
    } else {
      return 'Sin restaurante';
    }
  }

  getProductName(productId: number | undefined): string {
    if (productId !== undefined) {
      return this.productsNames[productId];
    } else {
      return 'Sin restaurante';
    }
  }

}
