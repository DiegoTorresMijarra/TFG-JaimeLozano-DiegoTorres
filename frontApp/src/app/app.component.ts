import { CommonModule } from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {
  IonApp,
  IonSplitPane,
  IonMenu,
  IonContent,
  IonList,
  IonListHeader,
  IonNote,
  IonMenuToggle,
  IonItem,
  IonIcon,
  IonLabel,
  IonRouterOutlet,
  IonRouterLink,
  IonButton
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  bookmarkOutline,
  bookmarkSharp,
  bagOutline, bagSharp, restaurantSharp, restaurantOutline
} from 'ionicons/icons';
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule, IonApp, IonSplitPane, IonMenu, IonContent, IonList, IonListHeader, IonNote, IonMenuToggle, IonItem, IonIcon, IonLabel, IonRouterLink, IonRouterOutlet, IonButton],
})
export class AppComponent implements OnInit {
  public appPages = [
    { title: 'Products', url: '/folder/products', icon: 'bag' },
    { title: 'Restaurants', url: '/folder/restaurants', icon: 'restaurant' }
  ];
  public labels = ['Family', 'Friends', 'Notes', 'Work', 'Travel', 'Reminders'];
  public isLoggedIn = false;
  constructor(private router: Router, public authService: AuthService) {
    addIcons({ bookmarkOutline, bookmarkSharp, bagOutline, bagSharp, restaurantOutline, restaurantSharp});
  }

  ngOnInit() {
    this.checkLoginStatus();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
    this.checkLoginStatus();
  }

  checkLoginStatus() {
    this.isLoggedIn = this.authService.isLoggedIn();
  }
}
