import {AfterViewInit, Component, DoCheck, OnInit, ViewChild} from '@angular/core'
import { IonContent, IonicModule } from '@ionic/angular'
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router'
import {CommonModule, NgForOf} from '@angular/common'
import { AppComponent } from '../app.component'
import {
  bagOutline,pricetagOutline,
  pricetagSharp,
  bagSharp, cubeOutline, cubeSharp, listOutline, listSharp, restaurantOutline,
  restaurantSharp, starOutline, starSharp,
} from 'ionicons/icons'
import { addIcons } from 'ionicons'
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-body',
  standalone: true,
  imports: [
    IonicModule,
    RouterLinkActive,
    NgForOf,
    RouterLink,
    RouterOutlet,
    CommonModule,
  ],
  templateUrl: './body.component.html',
  styleUrl: './body.component.css',
})
export class BodyComponent implements OnInit, DoCheck {
  public rolesList: string[] | null = [];
  public appPages = [
    { title: 'Productos', url: '/products', icon: 'bag', role: '' }, // Disponible para todos los usuarios
    { title: 'Restaurantes', url: '/restaurants', icon: 'restaurant', role: '' }, // Disponible para todos los usuarios
    { title: 'Categorias', url: '/categories', icon: 'list', role: '' }, // Disponible para todos los usuarios
    { title: 'Valoraciones', url: '/evaluations', icon: 'star', role: 'ROLE_ADMIN' },// Solo admins
    { title: 'Pedidos', url: '/orders', icon: 'cube', role: 'ROLE_WORKER' },// workers y admins
    { title: 'Ofertas', url: '/offers', icon: 'pricetag', role: 'ROLE_ADMIN' },
  ];

  constructor(private authService: AuthService) {
    addIcons({ pricetagOutline,
      pricetagSharp,bagOutline, bagSharp, restaurantOutline, restaurantSharp, listOutline, listSharp, starOutline, starSharp, cubeOutline, cubeSharp })
  }
  ngOnInit() {
    this.rolesList = this.authService.getUserRoles();
  }
  ngDoCheck(): void {
    this.rolesList = this.authService.getUserRoles();
  }

  hasRole(role: string): boolean {
    return <boolean>this.rolesList?.includes(role);
  }

  protected readonly AppComponent = AppComponent
}
