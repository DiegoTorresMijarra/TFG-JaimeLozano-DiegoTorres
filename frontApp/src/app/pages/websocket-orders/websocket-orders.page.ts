import { Component, OnInit } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import { WebSocketService } from '../../services/websocketOrders.service'
import { Notification } from '../../models/notification.entity'
import { IonicModule } from '@ionic/angular'

@Component({
  selector: 'app-websocket-orders',
  templateUrl: './websocket-orders.page.html',
  styleUrls: ['./websocket-orders.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule],
})
export class WebsocketOrdersPage implements OnInit {
  notifications: Notification[] = []

  constructor(private wsService: WebSocketService) {}

  ngOnInit(): void {
    const localStorageNotifications = localStorage.getItem('notifications')
    if (localStorageNotifications) {
      this.notifications = JSON.parse(localStorageNotifications)
    }
    this.wsService.getMessages().subscribe((message: Notification) => {
      if (message.entity === 'ORDERS') {
        this.notifications.push(message)
      }
    })
  }
}
