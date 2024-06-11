import { Component, inject, OnInit } from '@angular/core'
import { CommonModule, NgOptimizedImage } from '@angular/common'
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import { ProductService } from '../../../services/product.service'
import { ActivatedRoute, Router } from '@angular/router'
import { AuthService } from '../../../services/auth.service' // Importa AuthService
import { AppComponent } from '../../../app.component' // Importa AppComponent
import { UserService } from '../../../services/user.service'
import {
  getProductUrl,
  Product,
  ValidImageExtensions,
} from '../../../models/product.entity'
import { IonicModule } from '@ionic/angular'

@Component({
  selector: 'app-update-image',
  templateUrl: './update-image.page.html',
  styleUrls: ['./update-image.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule,
    NgOptimizedImage,
  ],
})
export class UpdateImagePage implements OnInit {
  private productId!: string
  protected product: Product | undefined
  private fileToUpload: File | null = null
  public imageForm!: FormGroup

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private appComponent: AppComponent,
    private userService: UserService,
  ) {}

  ngOnInit() {
    this.productId = this.route.snapshot.paramMap.get('id')!
    this.productService.getProduct(this.productId).subscribe({
      next: (product: Product) => {
        this.product = product
      },
      error: (error) => {
        console.error('Error obteniendo el producto:', error)
      },
    })

    this.imageForm = this.fb.group({
      image: [null, [Validators.required, this.fileValidator]],
    })
  }

  handleFileInput(event: Event) {
    const input = event.target as HTMLInputElement
    if (input.files && input.files.length) {
      this.imageForm.patchValue({ image: input.files[0] })
      this.imageForm.get('image')?.updateValueAndValidity()
      this.fileToUpload = input.files[0]
      console.log(this.fileToUpload)
    }
  }

  onSubmitImage() {
    if (this.fileToUpload) {
      this.productService
        .updateProductPhoto(Number.parseInt(this.productId), this.fileToUpload)
        .subscribe({
          next: (response) => {
            console.log('Imagen del producto actualizada:', response)
            this.router.navigate(['/products'])
          },
          error: (error) => {
            console.error('Error actualizando la imagen del producto:', error)
          },
        })
    }
  }

  fileValidator(control: FormControl): { [key: string]: any } | null {
    const file = control.value
    if (file) {
      const isValid = ValidImageExtensions.has(file.type)
      return isValid ? null : { invalidMimeType: true }
    }
    return null
  }

  protected readonly getProductUrl = getProductUrl
  protected readonly ValidImageExtensions = ValidImageExtensions
  protected readonly JSON = JSON
}
