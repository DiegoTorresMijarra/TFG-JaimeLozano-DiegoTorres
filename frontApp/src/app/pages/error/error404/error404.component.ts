import { Component, OnInit } from '@angular/core';
import {IonicModule} from "@ionic/angular";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-error404',
  templateUrl: './error404.component.html',
  styleUrls: ['./error404.component.scss'],
  imports: [
    IonicModule,
    RouterLink
  ],
  standalone: true
})
export class Error404Component  {

  constructor() { }

}
