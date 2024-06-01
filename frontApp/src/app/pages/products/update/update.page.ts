import { Component, inject, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput,
  IonItem,
  IonLabel,
  IonNote,
  IonSelect,
  IonSelectOption,
  IonText,
  IonTitle,
  IonToggle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { CategoryService } from '../../../services/category.service'
import { ProductService } from '../../../services/product.service'
import { ActivatedRoute, Router } from '@angular/router'
import { Category } from '../../../models/category.entity'
import { Product, ProductSaveDto } from '../../../models/product.entity'

@Component({
  selector: 'app-update',
  templateUrl: './update.page.html',
  styleUrls: ['./update.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    CommonModule,
    FormsModule,
    IonButton,
    IonInput,
    IonItem,
    IonLabel,
    IonNote,
    IonSelect,
    IonSelectOption,
    IonText,
    IonToggle,
    ReactiveFormsModule,
  ],
})
export class UpdatePage implements OnInit {
  private productId!: string
  public categories: Category[] = []
  private productService = inject(ProductService)
  private categoryService = inject(CategoryService)
  productForm!: FormGroup

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.productId = this.route.snapshot.paramMap.get('id')!
    this.loadCategories()
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(50)]],
      price: [
        '',
        [
          Validators.required,
          Validators.min(0),
          Validators.pattern(/^\d+(\.\d{1,2})?$/),
        ],
      ],
      stock: ['', [Validators.required, Validators.min(0)]],
      gluten: [true],
      categoryId: [1, [Validators.min(0)]],
    })
    this.loadProductData()
  }

  loadProductData() {
    this.productService.getProduct(this.productId).subscribe({
      next: (product: Product) => {
        this.productForm.patchValue({
          name: product.name,
          price: product.price,
          stock: product.stock,
          gluten: product.gluten,
          categoryId: product.category.id,
        })
      },
      error: (error: any) => {
        console.error('Error cargando el restaurante:', error)
      },
    })
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe((data) => {
      this.categories = data
    })
  }

  onSubmit() {
    if (this.productForm.valid) {
      const newProduct: ProductSaveDto = this.productForm.value

      this.productService.updateProduct(this.productId, newProduct).subscribe({
        next: (response: Product) => {
          // Lógica en caso de éxito
          console.log('Producto actualizado:', response)
          this.router.navigate(['/products'])
        },
        error: (error: any) => {
          // Manejo de errores
          console.error('Error actualizando producto:', error)
          // Mostrar una notificación de error o cualquier otra acción necesaria
        },
      })
    }
  }
}
