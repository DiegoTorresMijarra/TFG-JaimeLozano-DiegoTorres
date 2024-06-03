import {Component, Input, OnInit} from '@angular/core';
import {ModalController} from "@ionic/angular";
import {addIcons} from "ionicons";
import {closeOutline, closeSharp, starOutline, starSharp} from "ionicons/icons";
import {Product} from "../../../models/product.entity";
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader, IonIcon,
  IonItem,
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
  imports: [
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    CommonModule,
    FormsModule,
    IonButton,
    IonButtons,
    IonItem,
    IonLabel,
    IonList,
    RouterLink,
    IonIcon,
    IonText,
  ],
})
export class ProductModalComponent {

  @Input() product: Product | undefined

  constructor(private modalController: ModalController) {
    addIcons({ closeOutline, closeSharp, starOutline, starSharp })
  }

  async dismissModal() {
    return await this.modalController.dismiss()
  }

}
