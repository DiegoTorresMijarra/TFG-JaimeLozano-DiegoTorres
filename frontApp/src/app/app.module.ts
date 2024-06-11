import { importProvidersFrom, NgModule } from '@angular/core'
import { BrowserModule } from '@angular/platform-browser'
import {
  provideRouter,
  RouteReuseStrategy,
  RouterLink,
  RouterLinkActive,
  RouterModule,
  RouterOutlet,
} from '@angular/router'

import { IonicModule, IonicRouteStrategy } from '@ionic/angular'

import { HeaderComponent } from './header/header.component'
import { BodyComponent } from './body/body.component'
import { FooterComponent } from './footer/footer.component'
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http'
import { ScrollingModule } from '@angular/cdk/scrolling'
import { AppComponent } from './app.component'
import { AppRoutingModule, routes } from './app.routes'
import { CommonModule } from '@angular/common'
import { Error400Component } from './pages/error/error400/error400.component'
import { Error403Component } from './pages/error/error403/error403.component'
import { Error404Component } from './pages/error/error404/error404.component'
import { HttpErrorService } from './interceptors/http-error.service'

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    IonicModule.forRoot(),
    IonicModule,
    AppRoutingModule,
    HeaderComponent,
    BodyComponent,
    FooterComponent,
    HttpClientModule,
    ScrollingModule,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    HeaderComponent,
    BodyComponent,
    FooterComponent,
    RouterOutlet,
    RouterModule,
    Error400Component,
    Error403Component,
    Error404Component,
  ],
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorService, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
