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
  IonToolbar
} from '@ionic/angular/standalone';
import {Product} from "../../../models/product.entity";
import {Router} from "@angular/router";
import {ProductService} from "../../../services/product.service";
import {OfferService} from "../../../services/offer.service";
import {OfferDto} from "../../../models/offer.entity";
import {PageResponse} from "../../../models/pageResponse.entity";

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
    private offerService: OfferService
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
      .getProducts()
      .subscribe((page: PageResponse<Product>) => {
        this.products = page.content
      })
  }

  onSubmit() {
    if (this.offerForm.valid) {
      const newOffer: OfferDto = this.offerForm.value;

      this.offerService.saveOffer(newOffer).subscribe({
        next: (response: any) => {
          console.log('Nueva Oferta guardada:', response);
          this.router.navigate(['/offers']); // Adjust the route as necessary
        },
        error: (error: any) => {
          console.error('Error guardando oferta:', error);
          // Show an error notification or take any other necessary action
        },
      });
    }
  }

}
