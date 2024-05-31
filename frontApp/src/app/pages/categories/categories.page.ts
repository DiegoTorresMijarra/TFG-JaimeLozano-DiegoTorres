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
import {AnimationController, ModalController} from "@ionic/angular";

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
  public isAdmin: boolean = false
  constructor(private modalController: ModalController, private animationCtrl: AnimationController) { }

  ngOnInit() {
    this.loadCategories()
    this.isAdmin = this.authService.getUserRole() === 'admin'
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
      enterAnimation: this.enterAnimation,
      leaveAnimation: this.leaveAnimation
    });
    return await modal.present();
  }

  enterAnimation = (baseEl: HTMLElement) => {
    const root = baseEl.shadowRoot;

    const backdropAnimation = this.animationCtrl
      .create()
      .addElement(root?.querySelector('ion-backdrop')!)
      .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

    const wrapperAnimation = this.animationCtrl
      .create()
      .addElement(root?.querySelector('.modal-wrapper')!)
      .keyframes([
        {offset: 0, opacity: '0', transform: 'scale(0)'},
        {offset: 1, opacity: '0.99', transform: 'scale(1)'},
      ]);

    return this.animationCtrl
      .create()
      .addElement(baseEl)
      .easing('ease-out')
      .duration(400)
      .addAnimation([backdropAnimation, wrapperAnimation]);
  };

  leaveAnimation = (baseEl: HTMLElement) => {
    return this.enterAnimation(baseEl).direction('reverse');
  };
}
