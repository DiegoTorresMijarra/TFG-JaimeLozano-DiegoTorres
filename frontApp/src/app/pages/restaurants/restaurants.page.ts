import { Component, inject, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonItem,
  IonLabel,
  IonList,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { RestaurantService } from '../../services/restaurant.service'
import { AuthService } from '../../services/auth.service'
import { RouterLink } from '@angular/router'
import { Restaurant } from '../../models/restaurant.entity'
import {AnimationService} from "../../services/animation.service";
import {ModalController} from "@ionic/angular";
import {Category} from "../../models/category.entity";
import {CategoryModalComponent} from "../categories/category-modal/category-modal.component";
import {RestaurantModalComponent} from "./modal/modal.component";

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
  private animationService = inject(AnimationService)
  public isAdmin: boolean = false
  constructor(private modalController: ModalController) {}

  ngOnInit() {
    this.loadRestaurants()
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN')
  }

  async openDetailsModal(restaurant: Restaurant) {
    await this.presentModal(restaurant)
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

  async presentModal(restaurant: Restaurant) {
    const modal = await this.modalController.create({
      component: RestaurantModalComponent,
      componentProps: {
        restaurant: restaurant,
      },
      enterAnimation: this.animationService.enterAnimation,
      leaveAnimation: this.animationService.leaveAnimation,
    })
    return await modal.present()
  }

}
