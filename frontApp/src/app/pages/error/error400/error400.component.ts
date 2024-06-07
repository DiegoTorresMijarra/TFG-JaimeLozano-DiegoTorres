import { Component, OnInit } from '@angular/core';
import {IonicModule} from "@ionic/angular";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-error400',
  templateUrl: './error400.component.html',
  styleUrls: ['./error400.component.scss'],
  imports: [
    IonicModule,
    RouterLink
  ],
  standalone: true
})
export class Error400Component  {

  constructor() { }

}
