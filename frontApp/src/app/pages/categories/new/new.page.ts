import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput,
  IonItem,
  IonLabel, IonNote, IonText,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Category, CategoryDto, CategoryService} from "../../../services/category.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new',
  templateUrl: './new.page.html',
  styleUrls: ['./new.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonInput, IonItem, IonLabel, IonNote, IonText, ReactiveFormsModule]
})
export class NewPage implements OnInit {
  categoryForm!: FormGroup;
  private categoryService = inject(CategoryService)
  constructor(private fb: FormBuilder, private router: Router) { }

  ngOnInit() {
    this.categoryForm = this.fb.group({
      name: [
        '',
        [
          Validators.required
        ]
      ]
    });
  }

  onSubmit() {
    if (this.categoryForm.valid) {
      const newCategory: CategoryDto = this.categoryForm.value;
      this.categoryService.saveCategory(newCategory)
        .subscribe({
          next: (response: Category) => {
            console.log('Nuevo Categoria guardado:', response);
            this.router.navigate(['/categories']);
          },
          error: (error: any) => {
            console.error('Error guardando categoria:', error);
          }
        });
    }
  }

}
