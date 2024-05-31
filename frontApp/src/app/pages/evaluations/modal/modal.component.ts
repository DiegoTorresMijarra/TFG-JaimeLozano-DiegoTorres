import {Component, Input, OnInit} from '@angular/core';
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
import {Evaluation} from "../../../services/evaluation.service";
import {ModalController} from "@ionic/angular";
import {addIcons} from "ionicons";
import {closeOutline, closeSharp} from "ionicons/icons";
import {EvaluationsPage} from "../evaluations.page";

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonButtons, IonItem, IonLabel, IonList, RouterLink, IonIcon, IonText]
})
export class EvaluationModalComponent {
  @Input() evaluation: Evaluation | undefined;

  constructor(private modalController: ModalController) {
    addIcons({closeOutline , closeSharp })
  }

  async dismissModal() {
    return await this.modalController.dismiss();
  }

}
