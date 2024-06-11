import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import { IonicModule } from '@ionic/angular'
import { Address, AddressDto } from '../../../models/address.entity'
import { AddressesService } from '../../../services/addresses.service'
import { Router } from '@angular/router'

@Component({
  selector: 'app-new-address',
  templateUrl: './new-address.page.html',
  styleUrls: ['./new-address.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, ReactiveFormsModule],
})
export class NewAddressPage implements OnInit {
  addressForm: FormGroup

  private readonly pattern = '^[a-zA-ZñÑáéíóúÁÉÍÓÚ ,.;:0-9\\s]*$'

  constructor(
    private formBuilder: FormBuilder,
    private addressService: AddressesService,
    private router: Router,
  ) {
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
  }

  ngOnInit() {}

  onSubmit() {
    if (this.addressForm.valid) {
      const newAddress: AddressDto = this.addressForm.value
      this.addressService.saveAddress(newAddress).subscribe({
        next: (response: Address) => {
          console.log('Nueva Dirección guardada:', response)
          this.router.navigate(['/addresses'])
        },
        error: (error: any) => {
          console.error('Error guardando dirección:', error)
        },
      })
    }
  }
}
