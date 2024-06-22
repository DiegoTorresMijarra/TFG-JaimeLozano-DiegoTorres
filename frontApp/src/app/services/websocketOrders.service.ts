import { Injectable } from '@angular/core'
import { Observable, Subject } from 'rxjs'
import { AuthService } from './auth.service'
import { Notification } from '../models/notification.entity'
import { environment } from '../../environments/environment'
import { environment as envProd } from '../../environments/environment.prod'

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private socket: WebSocket
  private messages: Subject<Notification> = new Subject<Notification>()
  private notifications: Notification[] = []

  constructor() {
    const url =
      'wss://' +
      (environment.production ? envProd.apiUrl : environment.apiUrl) +
      '/ws/orders'
    this.socket = new WebSocket(url)
  }

  connect() {
    this.socket.onopen = () => {
      console.log('WebSocket connection opened')
    }

    this.socket.onmessage = (event) => {
      const notificationLocalStorage = localStorage.getItem('notifications')
      if (notificationLocalStorage)
        this.notifications = JSON.parse(notificationLocalStorage)
      try {
        const notification = JSON.parse(event.data) as Notification
        this.messages.next(notification)
        this.notifications.push(notification)
        this.saveNotificationsToLocal()
      } catch (error) {
        console.log(event.data)
      }
    }

    this.socket.onclose = (event) => {
      console.error('WebSocket connection closed', event)
      // Intenta reconectar después de 5 segundos
      setTimeout(() => this.connect(), 5000)
    }

    this.socket.onerror = (error) => {
      console.error('WebSocket error', error)
      // Cierra el socket para desencadenar el evento onclose y reconectar
      // this.socket.close();
    }
  }

  private saveNotificationsToLocal() {
    localStorage.setItem('notifications', JSON.stringify(this.notifications))
  }

  getMessages() {
    return this.messages.asObservable()
  }

  clearNotifications() {
    this.notifications = []
    localStorage.removeItem('notifications')
  }
}
