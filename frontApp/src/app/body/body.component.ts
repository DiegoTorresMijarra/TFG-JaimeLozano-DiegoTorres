import {AfterViewInit, Component, DoCheck, OnInit, ViewChild} from '@angular/core'
import { IonContent, IonicModule } from '@ionic/angular'
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router'
import {CommonModule, NgForOf} from '@angular/common'
import { AppComponent } from '../app.component'
import {
  bagOutline,
  bagSharp, listOutline, listSharp, optionsOutline, optionsSharp,
  restaurantOutline,
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
  public userRole: string | null = null;
  public appPages = [
    { title: 'Productos', url: '/products', icon: 'bag', role: '' }, // Disponible para todos los usuarios
    { title: 'Restaurantes', url: '/restaurants', icon: 'restaurant', role: '' }, // Disponible para todos los usuarios
    { title: 'Categorias', url: '/categories', icon: 'list', role: '' }, // Disponible para todos los usuarios
    { title: 'Valoraciones', url: '/evaluations', icon: 'star', role: 'admin' }, // Solo admins
  ];

  constructor(private authService: AuthService) {
    addIcons({ bagOutline, bagSharp, restaurantOutline, restaurantSharp, listOutline, listSharp, starOutline, starSharp })
  }
  ngOnInit() {
    this.userRole = this.authService.getUserRole();
  }
  ngDoCheck(): void {
    this.userRole = this.authService.getUserRole();
  }
  protected readonly AppComponent = AppComponent
}
