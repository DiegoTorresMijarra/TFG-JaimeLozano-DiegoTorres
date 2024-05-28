import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  IonHeader,
  IonToolbar,
  IonButtons,
  IonMenuButton,
  IonTitle,
  IonContent,
  IonList, IonItem, IonLabel
} from '@ionic/angular/standalone';
import {Product, ProductService} from "../services/product.service";
import {CommonModule, DatePipe, NgFor} from "@angular/common";
import {Restaurant, RestaurantService} from "../services/restaurant.service";

@Component({
  selector: 'app-folder',
  templateUrl: './folder.page.html',
  styleUrls: ['./folder.page.scss'],
  standalone: true,
  imports: [IonHeader, IonToolbar, IonButtons, IonMenuButton, IonTitle, IonContent, DatePipe, IonList, IonItem, IonLabel, CommonModule, NgFor],
})
export class FolderPage implements OnInit {
  public products: Product[] = [];
  public restaurants: Restaurant[] = [];
  public folder!: string;
  private activatedRoute = inject(ActivatedRoute);
  private productService = inject(ProductService);
  private restaurantService = inject(RestaurantService);

  constructor() {}

  ngOnInit() {
    this.folder = this.activatedRoute.snapshot.paramMap.get('id') as string;
    this.loadProducts();
    this.loadRestaurants();
  }

  loadProducts() {
    this.productService.getProducts().subscribe((data) => {
      this.products = data;
    });
  }

  loadRestaurants() {
    this.restaurantService.getRestaurants().subscribe((data) => {
      this.restaurants = data;
    });
  }

  isRestaurantPage(): boolean {
    return this.folder === 'restaurants';
  }
  isProductsPage(): boolean {
    return this.folder === 'products';
  }
}
