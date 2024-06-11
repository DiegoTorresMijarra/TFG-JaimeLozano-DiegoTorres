import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton, IonCol,
  IonContent,
  IonDatetime, IonGrid,
  IonHeader, IonInput,
  IonItem,
  IonLabel, IonNote, IonRow, IonSelect, IonSelectOption,
  IonText,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone';
import {Product} from "../../../models/product.entity";
import {Router} from "@angular/router";
import {ProductService} from "../../../services/product.service";
import {OfferService} from "../../../services/offer.service";
import {Offer, OfferDto} from "../../../models/offer.entity";
import {catchError, of, switchMap} from "rxjs";
import {AlertController} from "@ionic/angular";

@Component({
  selector: 'app-new',
  templateUrl: './new.page.html',
  styleUrls: ['./new.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonItem, IonLabel, IonDatetime, IonText, IonNote, IonButton, IonInput, IonSelectOption, IonSelect, ReactiveFormsModule, IonCol, IonRow, IonGrid]
})
export class NewPage implements OnInit {

  public products: Product[] = [];
  offerForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private productService: ProductService,
    private offerService: OfferService,
    private alertController: AlertController
  ) {
  }

  ngOnInit() {
    this.loadProducts();
    this.offerForm = this.fb.group({
      productId: ['', [Validators.required, Validators.min(1)]],
      descuento: ['', [Validators.required, Validators.max(100), Validators.min(0)]],
      fechaDesde: ['', Validators.required],
      fechaHasta: ['', Validators.required],
    });
  }

  loadProducts() {
    this.productService
      .getProductsList()
      .subscribe((products: Product[]) => {
        this.products = products
      })
  }

  async presentAlert(header: string, message: string) {
    const alert = await this.alertController.create({
      header: header,
      message: message,
      buttons: ['OK']
    });

    await alert.present();
  }

  onSubmit() {
    if (this.offerForm.valid) {
      const newOffer: OfferDto = this.offerForm.value;

      this.offerService.getActiveOfferByProductId(newOffer.productId).pipe(
        switchMap((activeOffer: Offer) => {
          if (activeOffer) {
            // Si ya existe una oferta activa, muestra un mensaje de error
            this.presentAlert('Error', 'Ya existe una oferta activa en ese producto');
            return of(null); // Detiene el flujo
          } else {
            // Si no existe una oferta activa, guarda la nueva oferta
            return this.offerService.saveOffer(newOffer);
          }
        }),
        catchError((error) => {
          console.error('Error verificando oferta activa:', error);
          this.presentAlert('Error', 'Error verificando oferta activa');
          return of(null); // Detiene el flujo
        })
      ).subscribe({
        next: (response: any) => {
          if (response) {
            console.log('Nueva Oferta guardada:', response);
            this.router.navigate(['/offers']); // Ajusta la ruta según sea necesario
          }
        },
        error: (error: any) => {
          console.error('Error guardando oferta:', error);
          this.presentAlert('Error', 'Error guardando oferta');
        }
      });
    }
  }

}
