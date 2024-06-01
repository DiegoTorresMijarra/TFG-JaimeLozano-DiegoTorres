import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonButtons,
  IonContent,
  IonHeader,
  IonItem,
  IonLabel, IonList,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {AuthService} from "../../services/auth.service";
import {Category, CategoryService} from "../../services/category.service";
import {RouterLink} from "@angular/router";
import {CategoryModalComponent} from "./category-modal/category-modal.component";
import {ModalController} from "@ionic/angular";
import {AnimationService} from "../../services/animation.service";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.page.html',
  styleUrls: ['./categories.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonButtons, IonItem, IonLabel, IonList, RouterLink]
})
export class CategoriesPage implements OnInit {
  public categories: Category[] = []
  private categoryService = inject(CategoryService)
  private authService = inject(AuthService)
  private animationService = inject(AnimationService)
  public isAdmin: boolean = false
  constructor(private modalController: ModalController) { }

  ngOnInit() {
    this.loadCategories()
    this.isAdmin = this.authService.hasRole("ROLE_ADMIN")
  }

  async openCategoryDetailsModal(category: Category) {
    await this.presentModal(category);
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe((data) => {
      this.categories = data
    })
  }

  deleteCategory(id: number | undefined): void {
    this.categoryService.deleteCategory(String(id)).subscribe({
      next: () => {
        console.log('Categoria borrado correctamente')
        this.categories = this.categories.filter(
          (product) => product.id !== Number(id),
        )
      },
      error: (error) => {
        console.error('Error deleting product:', error)
        // Manejar el error según sea necesario
      },
    })
  }

  async presentModal(category: Category) {
    const modal = await this.modalController.create({
      component: CategoryModalComponent,
      componentProps: {
        category: category
      },
      enterAnimation: this.animationService.enterAnimation,
      leaveAnimation: this.animationService.leaveAnimation
    });
    return await modal.present();
  }
}
