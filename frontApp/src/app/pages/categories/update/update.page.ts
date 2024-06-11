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
  IonText,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { CategoryService } from '../../../services/category.service'
import { ActivatedRoute, Router } from '@angular/router'
import { Category, CategoryDto } from '../../../models/category.entity'

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
    IonText,
    ReactiveFormsModule,
  ],
})
export class UpdatePage implements OnInit {
  private categoryId!: string
  categoryForm!: FormGroup
  private categoryService = inject(CategoryService)

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.categoryId = this.route.snapshot.paramMap.get('id')!

    this.categoryForm = this.fb.group({
      name: ['', [Validators.required]],
    })

    this.loadCategoryData()
  }

  loadCategoryData() {
    this.categoryService.getCategory(this.categoryId).subscribe({
      next: (category: Category) => {
        this.categoryForm.patchValue({
          name: category.name,
        })
      },
      error: (error: any) => {
        console.error('Error cargando la categoría:', error)
      },
    })
  }

  onSubmit() {
    if (this.categoryForm.valid) {
      const newCategory: CategoryDto = this.categoryForm.value
      this.categoryService
        .updateCategory(this.categoryId, newCategory)
        .subscribe({
          next: (response: Category) => {
            console.log('Categoria actualizada:', response)
            this.router.navigate(['/categories'])
          },
          error: (error: any) => {
            console.error('Error actualizando categoria:', error)
          },
        })
    }
  }
}
