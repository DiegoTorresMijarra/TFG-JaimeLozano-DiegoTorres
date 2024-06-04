import {Component, inject, Input, OnInit} from '@angular/core';
import {ModalController} from "@ionic/angular";
import {addIcons} from "ionicons";
import {closeOutline, closeSharp, starOutline, starSharp} from "ionicons/icons";
import {Product} from "../../../models/product.entity";
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
import {EvaluationResponseDto} from "../../../models/evaluation.entity";
import {EvaluationService} from "../../../services/evaluation.service";

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
    IonItemDivider,
  ],
})
export class ProductModalComponent implements OnInit {
  public evaluations: EvaluationResponseDto[] = []
  private evaluationService = inject(EvaluationService)
  @Input() product: Product | undefined

  constructor(private modalController: ModalController) {
    addIcons({ closeOutline, closeSharp, starOutline, starSharp })
  }

  ngOnInit(){
    this.loadEvaluations(this.product?.id)
  }

  async dismissModal() {
    return await this.modalController.dismiss()
  }

  loadEvaluations(id: number | undefined){
    this.evaluationService.getEvaluationsByProductId(id).subscribe((data) => {
      this.evaluations = data
    })
  }
}
