import { Component, OnInit } from '@angular/core';
import {IonicModule} from "@ionic/angular";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-error403',
  templateUrl: './error403.component.html',
  styleUrls: ['./error403.component.scss'],
  imports: [
    IonicModule,
    RouterLink
  ],
  standalone: true
})
export class Error403Component  {

  constructor() { }

}
