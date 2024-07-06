import { IonicModule } from '@ionic/angular'
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router'
import { CommonModule, NgForOf } from '@angular/common'
import { Component, DoCheck, OnInit } from '@angular/core'
import { AuthService } from '../services/auth.service'
import {StatusResponseEntity} from "../models/statusResponse.entity";
import {BackStatusService} from "../services/backStatus.service";

@Component({
  selector: 'app-body',
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
  standalone: true,
})
export class BodyComponent implements OnInit, DoCheck {
  public rolesList: string[] | null = []
  protected backStatus: StatusResponseEntity = new StatusResponseEntity()

  public appPages = [
    { title: 'Productos', url: '/products', icon: 'bag', role: '' }, // Disponible para todos los usuarios
    {
      title: 'Restaurantes',
      url: '/restaurants',
      icon: 'restaurant',
      role: '',
    }, // Disponible para todos los usuarios
    {
      title: 'Categorias',
      url: '/categories',
      icon: 'list',
      role: 'ROLE_WORKER',
    },
    { title: 'Menú', url: '/menu', icon: 'fast-food', role: '' },
    {
      title: 'Valoraciones',
      url: '/evaluations',
      icon: 'star',
      role: 'ROLE_WORKER',
    },
    { title: 'Pedidos', url: '/orders', icon: 'cube', role: 'ROLE_WORKER' }, // workers y admins
    { title: 'Ofertas', url: '/offers', icon: 'pricetag', role: 'ROLE_WORKER' },
  ]

  constructor(private authService: AuthService, private backStatusService : BackStatusService) {}

  ngOnInit() {
    this.rolesList = this.authService.getUserRoles()
    this.backStatusService.getBadgeStatus().subscribe(backStatus => {
      this.backStatus = backStatus
    })
  }

  ngDoCheck(): void {
    this.rolesList = this.authService.getUserRoles()
  }

  hasRole(role: string): boolean {
    return <boolean>this.rolesList?.includes(role)
  }
}
