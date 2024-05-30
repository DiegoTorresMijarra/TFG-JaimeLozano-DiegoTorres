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
  constructor() {
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
}
