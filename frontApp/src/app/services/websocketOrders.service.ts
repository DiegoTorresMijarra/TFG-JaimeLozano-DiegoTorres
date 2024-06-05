import { Injectable } from '@angular/core'
import { Observable, Subject } from 'rxjs'
import { AuthService } from './auth.service'
import { Notification } from '../models/notification.entity'

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private socket: WebSocket
  private messages: Subject<Notification> = new Subject<Notification>()

  constructor() {
    this.socket = new WebSocket('wss://localhost:3000/v1/ws/orders')

    this.socket.onmessage = (event) => {
      this.messages.next(JSON.parse(event.data) as Notification)
    }

    this.socket.onopen = () => {
      console.log('WebSocket connection opened')
    }

    this.socket.onclose = () => {
      console.log('WebSocket connection closed')
    }
  }

  getMessages(): Observable<Notification> {
    return this.messages.asObservable()
  }
}
