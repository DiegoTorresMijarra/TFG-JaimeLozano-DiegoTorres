import { Component, inject, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput,
  IonItem,
  IonLabel,
  IonNote,
  IonText,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { Router } from '@angular/router'
import { ProductService } from '../../../services/product.service'
import { RestaurantService } from '../../../services/restaurant.service'
import { Restaurant, RestaurantDto } from '../../../models/restaurant.entity'

@Component({
  selector: 'app-new',
  templateUrl: './new.page.html',
  styleUrls: ['./new.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    CommonModule,
    FormsModule,
    IonItem,
    IonLabel,
    IonInput,
    IonText,
    IonNote,
    IonButton,
    ReactiveFormsModule,
  ],
})
export class NewPage implements OnInit {
  restaurantForm!: FormGroup
  private restaurantService = inject(RestaurantService)

  constructor(
    private fb: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit() {
    this.restaurantForm = this.fb.group({
      name: ['', [Validators.required]],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      phone: ['', [Validators.required, Validators.pattern('\\d{9}')]],
    })
  }

  onSubmit() {
    if (this.restaurantForm.valid) {
      const newRestaurant: RestaurantDto = this.restaurantForm.value
      this.restaurantService.saveRestaurant(newRestaurant).subscribe({
        next: (response: Restaurant) => {
          console.log('Nuevo Restaurante guardado:', response)
          this.router.navigate(['/restaurants'])
        },
        error: (error: any) => {
          console.error('Error guardando restaurante:', error)
        },
      })
    }
  }
}
