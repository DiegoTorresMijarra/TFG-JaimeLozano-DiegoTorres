import { Component, inject, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonButton, IonButtons,
  IonContent,
  IonHeader,
  IonItem,
  IonLabel,
  IonList,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import {
  Restaurant,
  RestaurantService,
} from '../../services/restaurant.service'
import {AuthService} from "../../services/auth.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-restaurants',
  templateUrl: './restaurants.page.html',
  styleUrls: ['./restaurants.page.scss'],
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
    IonList,
    IonButton,
    IonButtons,
    RouterLink,
  ],
})
export class RestaurantsPage implements OnInit {
  public restaurants: Restaurant[] = []
  private restaurantService = inject(RestaurantService)
  private authService = inject(AuthService)
  public isAdmin: boolean = false
  constructor() {}

  ngOnInit() {
    this.loadRestaurants()
    this.isAdmin = this.authService.getUserRole() === 'admin'
  }

  loadRestaurants() {
    this.restaurantService.getRestaurants().subscribe((data) => {
      this.restaurants = data
    })
  }

  deleteRestaurant(id: number | undefined): void {
    this.restaurantService.deleteRestaurant(String(id)).subscribe({
      next: () => {
        console.log('Restaurante eliminado correctamente')
        // Elimina el producto de la lista después de eliminarlo
        this.restaurants = this.restaurants.filter(
          (restaurant) => restaurant.id !== Number(id),
        )
      },
      error: (error) => {
        console.error('Error borrando restaurante:', error)
        // Manejar el error según sea necesario
      },
    })
  }
}
