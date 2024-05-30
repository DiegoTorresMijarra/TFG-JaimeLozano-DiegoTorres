import { AfterViewInit, Component, ViewChild } from '@angular/core'
import { IonContent, IonicModule } from '@ionic/angular'
import { ContentHeaderComponent } from './content-header/content-header.component'
import { ContentComponent } from './content/content.component'
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router'
import { NgForOf } from '@angular/common'
import { AppComponent } from '../app.component'
import {
  bagOutline,
  bagSharp, listOutline, listSharp, optionsOutline, optionsSharp,
  restaurantOutline,
  restaurantSharp,
} from 'ionicons/icons'
import { addIcons } from 'ionicons'

@Component({
  selector: 'app-body',
  standalone: true,
  imports: [
    ContentHeaderComponent,
    IonicModule,
    ContentComponent,
    ContentHeaderComponent,
    ContentComponent,
    RouterLinkActive,
    NgForOf,
    RouterLink,
    RouterOutlet,
  ],
  templateUrl: './body.component.html',
  styleUrl: './body.component.css',
})
export class BodyComponent {
  //@ViewChild(AppComponent) body: IonContent;

  public appPages = [
    { title: 'Productos', url: '/products', icon: 'bag' },
    { title: 'Restaurantes', url: '/restaurants', icon: 'restaurant' },
    { title: 'Categorias', url: '/categories', icon: 'list'}
  ]
  public labels = ['Family', 'Friends', 'Notes', 'Work', 'Travel', 'Reminders']
  public isLoggedIn = false

  constructor() {
    addIcons({ bagOutline, bagSharp, restaurantOutline, restaurantSharp, listOutline, listSharp })
  }

  //scrollTo() {
  //      this.body?.scrollToTop().then(r => true);
  //}

  protected readonly AppComponent = AppComponent
}
