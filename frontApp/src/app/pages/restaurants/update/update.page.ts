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
import { RestaurantService } from '../../../services/restaurant.service'
import { ActivatedRoute, Router } from '@angular/router'
import { Restaurant, RestaurantDto } from '../../../models/restaurant.entity'

@Component({
  selector: 'app-update',
  templateUrl: './update.page.html',
  styleUrls: ['./update.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    CommonModule,
    FormsModule,
    IonButton,
    IonInput,
    IonItem,
    IonLabel,
    IonNote,
    IonText,
    ReactiveFormsModule,
  ],
})
export class UpdatePage implements OnInit {
  private restaurantId!: string
  restaurantForm!: FormGroup
  private restaurantService = inject(RestaurantService)

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.restaurantId = this.route.snapshot.paramMap.get('id')!
    this.restaurantForm = this.fb.group({
      name: ['', [Validators.required]],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      phone: ['', [Validators.required, Validators.pattern('\\d{9}')]],
    })
    this.loadRestaurantData()
  }

  loadRestaurantData() {
    this.restaurantService.getRestaurant(this.restaurantId).subscribe({
      next: (restaurant: Restaurant) => {
        this.restaurantForm.patchValue({
          name: restaurant.name,
          address: restaurant.address,
          phone: restaurant.phone,
        })
      },
      error: (error: any) => {
        console.error('Error cargando el restaurante:', error)
      },
    })
  }

  onSubmit() {
    if (this.restaurantForm.valid) {
      const newRestaurant: RestaurantDto = this.restaurantForm.value
      this.restaurantService
        .updateRestaurant(this.restaurantId, newRestaurant)
        .subscribe({
          next: (response: Restaurant) => {
            console.log('Restaurante actualizado:', response)
            this.router.navigate(['/restaurants'])
          },
          error: (error: any) => {
            console.error('Error actualizando restaurante:', error)
          },
        })
    }
  }
}
