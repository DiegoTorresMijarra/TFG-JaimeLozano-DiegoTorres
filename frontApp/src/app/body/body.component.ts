import { AfterViewInit, Component, ViewChild } from '@angular/core'
import { IonContent, IonicModule } from '@ionic/angular'
import { ContentHeaderComponent } from './content-header/content-header.component'
import { ContentComponent } from './content/content.component'
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgForOf} from "@angular/common";
import {AppComponent} from "../app.component";

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
  ],
  templateUrl: './body.component.html',
  styleUrl: './body.component.css',
})
export class BodyComponent {
  //  @ViewChild(AppComponent) body: IonContent;

  public appPages = [
    { title: 'Products', url: '/folder/products', icon: 'bag' },
    { title: 'Restaurants', url: '/folder/restaurants', icon: 'restaurant' }
  ];
  public labels = ['Family', 'Friends', 'Notes', 'Work', 'Travel', 'Reminders'];
  public isLoggedIn = false;

  constructor() {}

  scrollTo() {
    //    this.body?.scrollToTop().then(r => true);
  }

  protected readonly AppComponent = AppComponent;
}
