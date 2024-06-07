import { CommonModule } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import {
  RouterLink,
  RouterLinkActive, RouterModule,
  RouterOutlet,
} from '@angular/router'
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
  IonButton,
  IonHeader,
  IonFooter,
} from '@ionic/angular/standalone'
import { HeaderComponent } from './header/header.component'
import { BodyComponent } from './body/body.component'
import { FooterComponent } from './footer/footer.component'
import { WebSocketService } from './services/websocketOrders.service'
import {Error404Component} from "./pages/error/error404/error404.component";
import {Error403Component} from "./pages/error/error403/error403.component";
import {Error400Component} from "./pages/error/error400/error400.component";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {HttpErrorService} from "./interceptors/http-error.service";


@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    CommonModule,
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
    IonRouterLink,
    IonRouterOutlet,
    IonButton,
    HeaderComponent,
    IonHeader,
    BodyComponent,
    IonFooter,
    FooterComponent,
    RouterOutlet,
    RouterModule,
    Error400Component,
    Error403Component,
    Error404Component,
  ],
})
export class AppComponent implements OnInit {
  constructor(private wsService: WebSocketService) {}

  ngOnInit(): void {
    this.wsService.connect()
  }
}
