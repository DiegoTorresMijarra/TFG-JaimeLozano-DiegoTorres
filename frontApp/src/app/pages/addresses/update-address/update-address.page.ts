import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import { Address, AddressDto } from '../../../models/address.entity'
import { AddressesService } from '../../../services/addresses.service'
import { ActivatedRoute, Router } from '@angular/router'
import { IonicModule } from '@ionic/angular'

@Component({
  selector: 'app-update-address',
  templateUrl: './update-address.page.html',
  styleUrls: ['./update-address.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule, ReactiveFormsModule],
})
export class UpdateAddressPage implements OnInit {
  private addressId!: string
  addressForm!: FormGroup
  // prettier-ignore
  private readonly pattern = '^[a-zA-ZñÑáéíóúÁÉÍÓÚ ,.;:0-9\\s]*$'

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private addressService: AddressesService,
  ) {}

  ngOnInit() {
    this.addressId = this.route.snapshot.paramMap.get('id')!

    this.addressForm = this.formBuilder.group({
      country: [
        'España',
        [
          Validators.required,
          Validators.maxLength(50),
          Validators.pattern(this.pattern),
        ],
      ],
      province: [
        'Madrid',
        [
          Validators.required,
          Validators.maxLength(50),
          Validators.pattern(this.pattern),
        ],
      ],
      city: [
        '',
        [
          Validators.required,
          Validators.maxLength(50),
          Validators.pattern(this.pattern),
        ],
      ],
      street: [
        '',
        [
          Validators.required,
          Validators.maxLength(100),
          Validators.pattern(this.pattern),
        ],
      ],
      number: [
        '',
        [
          Validators.required,
          Validators.maxLength(10),
          Validators.pattern('[0-9]*'),
        ],
      ],
      apartment: [
        '',
        [Validators.maxLength(50), Validators.pattern(this.pattern)],
      ],
      postalCode: ['', [Validators.required, Validators.pattern('[0-9]{5}')]],
      extraInfo: [
        '',
        [Validators.maxLength(255), Validators.pattern(this.pattern)],
      ],
      name: [
        '',
        [
          Validators.required,
          Validators.maxLength(100),
          Validators.pattern(this.pattern),
        ],
      ],
    })

    this.loadAddressData()
  }

  getFormErrors(): string[] {
    const errors: string[] = []
    Object.keys(this.addressForm.controls).forEach((key) => {
      const controlErrors = this.addressForm.get(key)?.errors
      if (controlErrors) {
        Object.keys(controlErrors).forEach((errorKey) => {
          switch (errorKey) {
            case 'required':
              errors.push(`${key} es requerido.`)
              break
            case 'maxlength':
              errors.push(`${key} excede la longitud máxima.`)
              break
            case 'pattern':
              errors.push(`${key} tiene un formato inválido.`)
              break
            default:
              errors.push(`${key} tiene un error.`)
          }
        })
      }
    })
    return errors
  }

  loadAddressData() {
    this.addressService.getAddress(this.addressId).subscribe({
      next: (address: Address) => {
        this.addressForm.patchValue(address)
      },
      error: (error: any) => {
        console.error('Error cargando la dirección:', error)
      },
    })
  }

  onSubmit() {
    if (this.addressForm.valid) {
      const updatedAddress: AddressDto = this.addressForm.value
      this.addressService
        .updateAddress(this.addressId, updatedAddress)
        .subscribe({
          next: (response: any) => {
            console.log('Dirección actualizada:', response)
            this.router.navigate(['/addresses'])
          },
          error: (error: any) => {
            console.error('Error actualizando dirección:', error)
          },
        })
    }
  }
}
