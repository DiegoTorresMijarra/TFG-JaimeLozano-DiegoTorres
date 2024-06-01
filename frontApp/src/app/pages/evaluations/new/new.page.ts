import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonHeader, IonIcon,
  IonInput,
  IonItem,
  IonLabel, IonNote, IonRange, IonSelect, IonSelectOption, IonText,
  IonTitle, IonToggle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Product, ProductService} from "../../../services/product.service";
import {Router} from "@angular/router";
import {Evaluation, EvaluationDto, EvaluationService} from "../../../services/evaluation.service";
import {addIcons} from "ionicons";
import {starOutline, starSharp} from "ionicons/icons";

@Component({
  selector: 'app-new',
  templateUrl: './new.page.html',
  styleUrls: ['./new.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonInput, IonItem, IonLabel, IonNote, IonSelect, IonSelectOption, IonText, IonToggle, ReactiveFormsModule, IonRange, IonIcon]
})
export class NewPage implements OnInit {
  public products: Product[] = []
  private productService = inject(ProductService)
  private evaluationService = inject(EvaluationService)
  evaluationForm!: FormGroup;
  constructor(private fb: FormBuilder, private router: Router) {
    addIcons({ starOutline, starSharp})
  }

  ngOnInit() {
    this.loadProducts()
    this.evaluationForm = this.fb.group({

      productId: [1, [Validators.min(0)]],
      value: [Validators.required],
      comment: ['', [Validators.required, Validators.maxLength(100)]]
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

      this.evaluationService.saveEvaluation(newEvaluation)
        .subscribe({
          next: (response: Evaluation) => {
            // Lógica en caso de éxito
            console.log('Nueva Valoracion guardado:', response);
            this.router.navigate(['/evaluations']); // Ajusta la ruta según sea necesario
          },
          error: (error: any) => {
            // Manejo de errores
            console.error('Error guardando valoracion:', error);
            // Mostrar una notificación de error o cualquier otra acción necesaria
          }
        });
    }
  }
}
