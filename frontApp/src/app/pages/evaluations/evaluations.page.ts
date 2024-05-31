import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader, IonIcon,
  IonItem,
  IonLabel, IonList,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Evaluation, EvaluationService} from "../../services/evaluation.service";
import {RouterLink} from "@angular/router";
import {addIcons} from "ionicons";
import {starOutline, starSharp} from "ionicons/icons";
import {Category} from "../../services/category.service";
import {CategoryModalComponent} from "../categories/category-modal/category-modal.component";
import {EvaluationModalComponent} from "./modal/modal.component";
import {AnimationService} from "../../services/animation.service";
import {ModalController} from "@ionic/angular";

@Component({
  selector: 'app-evaluations',
  templateUrl: './evaluations.page.html',
  styleUrls: ['./evaluations.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonButtons, IonItem, IonLabel, IonList, IonIcon, RouterLink]
})
export class EvaluationsPage implements OnInit {
  public evaluations: Evaluation[] = []
  private evaluationService = inject(EvaluationService)
  private animationService = inject(AnimationService)
  constructor(private modalController: ModalController) {
    addIcons({ starOutline, starSharp})
  }

  ngOnInit() {
    this.loadEvaluations()
  }

  loadEvaluations() {
    this.evaluationService.getEvaluations().subscribe((data) => {
      this.evaluations = data
    })
  }

  async openDetailsModal(evaluation: Evaluation) {
    await this.presentModal(evaluation);
  }

  deleteEvaluation(id: number | undefined): void {
    this.evaluationService.deleteEvaluation(String(id)).subscribe({
      next: () => {
        console.log('Valoracion borrada correctamente')
        this.evaluations = this.evaluations.filter(
          (product) => product.id !== Number(id),
        )
      },
      error: (error) => {
        console.error('Error borrando valoracion:', error)
        // Manejar el error según sea necesario
      },
    })
  }

  async presentModal(evaluation: Evaluation) {
    const modal = await this.modalController.create({
      component: EvaluationModalComponent,
      componentProps: {
        evaluation: evaluation
      },
      enterAnimation: this.animationService.enterAnimation,
      leaveAnimation: this.animationService.leaveAnimation
    });
    return await modal.present();
  }
}
