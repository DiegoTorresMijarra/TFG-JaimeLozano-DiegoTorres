import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
    IonButton,
    IonCol,
    IonContent,
    IonDatetime,
    IonGrid,
    IonHeader, IonInput, IonItem, IonLabel, IonNote, IonRow, IonSelect, IonSelectOption, IonText,
    IonTitle,
    IonToolbar
} from '@ionic/angular/standalone';
import {Product} from "../../../models/product.entity";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductService} from "../../../services/product.service";
import {OfferService} from "../../../services/offer.service";
import {Offer, OfferDto} from "../../../models/offer.entity";
import {Evaluation} from "../../../models/evaluation.entity";

@Component({
  selector: 'app-update',
  templateUrl: './update.page.html',
  styleUrls: ['./update.page.scss'],
  standalone: true,
    imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonCol, IonDatetime, IonGrid, IonInput, IonItem, IonLabel, IonNote, IonRow, IonSelect, IonSelectOption, IonText, ReactiveFormsModule]
})
export class UpdatePage implements OnInit {
  private offerId!: string
  public products: Product[] = [];
  offerForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private productService: ProductService,
    private offerService: OfferService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.loadProducts();
    this.offerId = this.route.snapshot.paramMap.get('id')!
    this.offerForm = this.fb.group({
      productId: ['', [Validators.required, Validators.min(1)]],
      descuento: ['', [Validators.required, Validators.max(100), Validators.min(0)]],
      fechaDesde: ['', Validators.required],
      fechaHasta: ['', Validators.required],
    });
    this.loadOfferData();
  }

  loadOfferData() {
    this.offerService.getOffer(this.offerId).subscribe({
      next: (offer: Offer) => {
        this.offerForm.patchValue({
          descuento: offer.descuento,
          productId: offer.product.id,
          fechaDesde: offer.fechaDesde,
          fechaHasta: offer.fechaHasta,
        })
      },
      error: (error: any) => {
        console.error('Error cargando la valoracion:', error)
      },
    })
  }

  loadProducts() {
    this.productService.getProducts().subscribe((data) => {
      this.products = data;
    });
  }

  onSubmit() {
    if (this.offerForm.valid) {
      const newOffer: OfferDto = this.offerForm.value;

      this.offerService.updateOffer(this.offerId, newOffer).subscribe({
        next: (response: any) => {
          console.log('Oferta actualizada:', response);
          this.router.navigate(['/offers']); // Adjust the route as necessary
        },
        error: (error: any) => {
          console.error('Error actualizando oferta:', error);
          // Show an error notification or take any other necessary action
        },
      });
    }
  }


}
