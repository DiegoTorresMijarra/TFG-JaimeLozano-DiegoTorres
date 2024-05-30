import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput,
  IonItem,
  IonLabel, IonNote, IonSelect, IonSelectOption,
  IonText,
  IonTitle, IonToggle,
  IonToolbar
} from '@ionic/angular/standalone';
import {Product, ProductSaveDto, ProductService} from "../../../services/product.service";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {Category, CategoryService} from "../../../services/category.service";

@Component({
  selector: 'app-new',
  templateUrl: './new.page.html',
  styleUrls: ['./new.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonItem, IonLabel, IonInput, IonText, IonNote, IonToggle, IonSelect, IonSelectOption, IonButton, ReactiveFormsModule]
})
export class NewPage implements OnInit{
  public categories: Category[] = []
  private productService = inject(ProductService)
  private categoryService = inject(CategoryService)
  productForm!: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit() {
    this.loadCategories()
    this.productForm = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],
      price: [
        '',
        [
          Validators.required,
          Validators.min(0),
          Validators.pattern(/^\d+(\.\d{1,2})?$/)
        ]
      ],
      stock: [
        '',
        [
          Validators.required,
          Validators.min(0)
        ]
      ],
      gluten: [true],
      categoryId: [1, [Validators.min(0)]]
    });
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe((data) => {
      this.categories = data
    })
  }

  onSubmit() {
    if (this.productForm.valid) {
      const newProduct: ProductSaveDto = this.productForm.value;

      this.productService.saveProduct(newProduct)
        .subscribe({
          next: (response: Product) => {
            // Lógica en caso de éxito
            console.log('Nuevo Producto guardado:', response);
            this.router.navigate(['/products']); // Ajusta la ruta según sea necesario
          },
          error: (error: any) => {
            // Manejo de errores
            console.error('Error guardando producto:', error);
            // Mostrar una notificación de error o cualquier otra acción necesaria
          }
        });
    }
  }

}
