import { Component, OnInit } from '@angular/core'
import { WebSocketService } from './services/websocketOrders.service'
import { catchError, throwError } from 'rxjs'
import {BackStatusService} from "./services/backStatus.service";
import {StatusResponseEntity} from "./models/statusResponse.entity";

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent implements OnInit {
  protected backStatus: StatusResponseEntity = new StatusResponseEntity()
  constructor(private wsService: WebSocketService, private backStatusService: BackStatusService) {}

  ngOnInit(): void {
    this.wsService.connect()
    this.backStatusService.getBadgeStatus().subscribe(backStatus => {
      this.backStatus = backStatus
    })
  }
}
