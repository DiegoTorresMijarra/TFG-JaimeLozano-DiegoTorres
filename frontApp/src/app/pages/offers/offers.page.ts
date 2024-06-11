import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonIcon,
  IonItem, IonLabel, IonList,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {AnimationService} from "../../services/animation.service";
import {ModalController} from "@ionic/angular";
import {Offer} from "../../models/offer.entity";
import {OfferService} from "../../services/offer.service";
import {OfferModalComponent} from "./modal/modal.component";
import {RouterLink} from "@angular/router";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-offers',
  templateUrl: './offers.page.html',
  styleUrls: ['./offers.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonButtons, IonIcon, IonItem, IonLabel, IonList, RouterLink]
})
export class OffersPage implements OnInit {

  public offers: Offer[] = []
  private authService = inject(AuthService)
  public isAdmin: boolean = false
  private offerService = inject(OfferService)
  private animationService = inject(AnimationService)
  constructor(private modalController: ModalController) { }

  ngOnInit() {
    this.loadOffers()
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN')
  }

  async openDetailsModal(offer: Offer) {
    await this.presentModal(offer)
  }

  loadOffers() {
    this.offerService.getOffers().subscribe((data) => {
      this.offers = data
    })
  }

  deleteOffer(id: number | undefined): void {
    this.offerService.deleteOffer(String(id)).subscribe({
      next: () => {
        console.log('Oferta borrada correctamente')
        this.offers = this.offers.filter(
          (offer) => offer.id !== Number(id),
        )
      },
      error: (error) => {
        console.error('Error borrando oferta:', error)
        // Manejar el error según sea necesario
      },
    })
  }

  async presentModal(offer: Offer) {
    const modal = await this.modalController.create({
      component: OfferModalComponent,
      componentProps: {
        offer: offer,
      },
      enterAnimation: this.animationService.enterAnimation,
      leaveAnimation: this.animationService.leaveAnimation,
    })
    return await modal.present()
  }

}
