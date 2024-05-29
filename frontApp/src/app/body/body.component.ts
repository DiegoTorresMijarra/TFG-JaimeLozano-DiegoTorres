import { AfterViewInit, Component, ViewChild } from '@angular/core'
import { IonContent, IonicModule } from '@ionic/angular'
import { ContentHeaderComponent } from './content-header/content-header.component'
import { ContentComponent } from './content/content.component'
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router'
import { NgForOf } from '@angular/common'
import { AppComponent } from '../app.component'
import {
  bagOutline,
  bagSharp,
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
    { title: 'Products', url: '/products', icon: 'bag' },
    { title: 'Restaurants', url: '/restaurants', icon: 'restaurant' },
  ]
  public labels = ['Family', 'Friends', 'Notes', 'Work', 'Travel', 'Reminders']
  public isLoggedIn = false

  constructor() {
    addIcons({ bagOutline, bagSharp, restaurantOutline, restaurantSharp })
  }

  //scrollTo() {
  //      this.body?.scrollToTop().then(r => true);
  //}

  protected readonly AppComponent = AppComponent
}
