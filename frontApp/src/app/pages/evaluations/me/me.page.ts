import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton, IonCard, IonCardContent, IonCardHeader, IonCardTitle,
  IonCol,
  IonContent, IonGrid,
  IonHeader, IonInput,
  IonItem, IonLabel, IonRow,
  IonText, IonTextarea,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {ActivatedRoute, Router} from "@angular/router";
import {EvaluationService} from "../../../services/evaluation.service";
import {Order} from "../../../models/order.entity";
import {EvaluationDto} from "../../../models/evaluation.entity";
import {OrderedProduct} from "../../../models/orderedProduct.entity";
import {catchError, forkJoin, Observable} from "rxjs";

@Component({
  selector: 'app-me',
  templateUrl: './me.page.html',
  styleUrls: ['./me.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonItem, IonText, IonButton, IonCol, IonRow, IonLabel, IonInput, IonTextarea, ReactiveFormsModule, IonGrid, IonCardContent, IonCardTitle, IonCardHeader, IonCard]
})
export class MePage implements OnInit{
  private evaluationService = inject(EvaluationService)
  evaluationForm!: FormGroup
  order: Order | undefined

  constructor(private fb: FormBuilder,
              private router: Router,
              private route: ActivatedRoute,) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state) {
      this.order = navigation.extras.state['order'];
    }
  }

  ngOnInit() {
    if (this.order) {
      this.initForm(this.order);
    } else {
      console.log("No existe la order")
    }
  }

  initForm(order: Order) {
    this.evaluationForm = this.fb.group({
      orderId: [order.id],
      evaluations: this.fb.array(order.orderedProducts.map(product => this.createProductEvaluationForm(product)))
    });
  }

  createProductEvaluationForm(product: OrderedProduct): FormGroup {
    return this.fb.group({
      productId: [{ value: product.productId, disabled: true }],
      value: ['', Validators.required],
      comment: ['']
    });
  }

  get evaluationForms(): FormArray {
    return this.evaluationForm.get('evaluations') as FormArray;
  }

  getEvaluationFormGroup(index: number): FormGroup {
    return this.evaluationForms.at(index) as FormGroup;
  }

  onSubmit() {
    if (this.evaluationForm.valid) {
      const evaluationDtos: EvaluationDto[] = this.evaluationForms.getRawValue().map((evalForm: any) => ({
        productId: evalForm.productId,
        value: evalForm.value,
        comment: evalForm.comment
      }));

      const saveObservables: Observable<any>[] = evaluationDtos.map(dto =>
        this.evaluationService.saveEvaluation(dto).pipe(
          catchError((error) => {
            console.error('Error guardando evaluation:', error);
            throw error;
          })
        )
      );

      forkJoin(saveObservables).subscribe({
        next: results => {
          console.log('Todas las evaluaciones fueron guardadas exitosamente', results);
          // Aquí podrías navegar a otra página o mostrar un mensaje de éxito
        },
        error: error => {
          console.error('Error guardando algunas evaluaciones:', error);
          // Aquí podrías manejar el error de manera más específica
        },
        complete: () => {
          console.log('Proceso de guardar evaluaciones completado.');
          this.router.navigate(['/me'])
        }
      });
    }
  }

}
