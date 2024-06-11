import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { IonicModule } from '@ionic/angular'
import { Address } from '../../../models/address.entity'
import { ActivatedRoute, Router } from '@angular/router'
import { AddressesService } from '../../../services/addresses.service'

@Component({
  selector: 'app-details',
  templateUrl: './details.page.html',
  styleUrls: ['./details.page.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class DetailsPage implements OnInit {
  address: Address | null = null

  constructor(
    private route: ActivatedRoute,
    private addressesService: AddressesService,
    private router: Router,
  ) {}

  ngOnInit() {
    const addressId = this.route.snapshot.paramMap.get('id')
    if (addressId) {
      this.getAddressDetails(addressId)
    }
  }

  getAddressDetails(id: string) {
    this.addressesService.getAddress(id).subscribe((address: Address) => {
      this.address = address
      if (!address) {
        console.error('Error obteniendo la dirección')
        this.router.navigate(['/'])
      }
    })
  }
}
