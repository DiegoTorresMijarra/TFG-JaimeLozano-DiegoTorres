import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel, IonNote, IonRange, IonSelect, IonSelectOption, IonText,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Product, ProductService} from "../../../services/product.service";
import {Evaluation, EvaluationDto, EvaluationService} from "../../../services/evaluation.service";
import {ActivatedRoute, Router} from "@angular/router";
import {addIcons} from "ionicons";
import {starOutline, starSharp} from "ionicons/icons";
import {Category} from "../../../services/category.service";

@Component({
  selector: 'app-update',
  templateUrl: './update.page.html',
  styleUrls: ['./update.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonIcon, IonItem, IonLabel, IonNote, IonRange, IonSelect, IonSelectOption, IonText, ReactiveFormsModule]
})
export class UpdatePage implements OnInit {
  private evaluationId!: string;
  public products: Product[] = []
  private productService = inject(ProductService)
  private evaluationService = inject(EvaluationService)
  evaluationForm!: FormGroup;
  constructor(private fb: FormBuilder, private router: Router, private route: ActivatedRoute) {
    addIcons({ starOutline, starSharp})
  }

  ngOnInit() {
    this.evaluationId = this.route.snapshot.paramMap.get('id')!;
    this.loadProducts()
    this.evaluationForm = this.fb.group({

      productId: [1, [Validators.min(0)]],
      valoracion: [Validators.required]
    });
    this.loadEvaluationData();
  }

  loadEvaluationData() {
    this.evaluationService.getEvaluation(this.evaluationId).subscribe({
      next: (evaluation: Evaluation) => {
        this.evaluationForm.patchValue({
          valoracion: evaluation.valoracion,
          productId: evaluation.product.id
        });
      },
      error: (error: any) => {
        console.error('Error cargando la valoracion:', error);
      }
    });
  }

  loadProducts() {
    this.productService.getProducts().subscribe((data) => {
      this.products = data
    })
  }

  onSubmit() {
    if (this.evaluationForm.valid) {
      const newEvaluation: EvaluationDto = this.evaluationForm.value;

      this.evaluationService.updateEvaluation(this.evaluationId, newEvaluation)
        .subscribe({
          next: (response: Evaluation) => {
            // Lógica en caso de éxito
            console.log('Valoracion actualizada:', response);
            this.router.navigate(['/evaluations']);
          },
          error: (error: any) => {
            // Manejo de errores
            console.error('Error actualizando valoracion:', error);
            // Mostrar una notificación de error o cualquier otra acción necesaria
          }
        });
    }
  }

}
