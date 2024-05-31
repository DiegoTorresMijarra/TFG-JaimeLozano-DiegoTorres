import {Component, Input} from '@angular/core';
import {AnimationController, ModalController} from "@ionic/angular";
import {Category} from "../../../services/category.service";
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
import {addIcons} from "ionicons";
import {closeOutline, closeSharp} from "ionicons/icons";

@Component({
  selector: 'app-category-modal',
  templateUrl: './category-modal.component.html',
  styleUrls: ['./category-modal.component.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonButtons, IonItem, IonLabel, IonList, RouterLink, IonIcon, IonText]
})
export class CategoryModalComponent {

  @Input() category: Category | undefined;

  constructor(private modalController: ModalController) {
    addIcons({closeOutline , closeSharp })
  }

  async dismissModal() {
    return await this.modalController.dismiss();
  }
}
