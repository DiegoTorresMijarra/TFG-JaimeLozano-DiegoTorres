// cart.service.ts
import { Injectable } from '@angular/core'
import { BehaviorSubject } from 'rxjs'
import { Cart } from '../models/cart.entity'
import { Product } from '../models/product.entity'

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cartSubject = new BehaviorSubject<Cart>({
    lineas: [],
    totalPrice: 0,
    totalQuantity: 0,
  })
  cart$ = this.cartSubject.asObservable()

  constructor() {
    const storedCart = localStorage.getItem('cart')
    if (storedCart) {
      this.cartSubject.next(JSON.parse(storedCart))
    }
  }

  getCart(): Cart {
    return this.cartSubject.value
  }

  addToCart(product: Product, price: number, quantity: number) {
    const cart = this.getCart()
    const existingLine = cart.lineas.find(
      (line) => line.product.id === product.id,
    )
    if (existingLine) {
      existingLine.quantity += quantity
      existingLine.subtotal += price * quantity
    } else {
      cart.lineas.push({
        product: product,
        price: price,
        quantity: quantity,
        subtotal: price * quantity,
      })
    }
    this.updateCart(cart)
  }

  removeFromCart(productId: number) {
    const cart = this.getCart()
    cart.lineas = cart.lineas.filter((line) => line.product.id !== productId)
    this.updateCart(cart)
  }

  private updateCart(cart: Cart) {
    cart.totalPrice = cart.lineas.reduce(
      (total, line) => total + line.subtotal,
      0,
    )
    cart.totalQuantity = cart.lineas.reduce(
      (total, line) => total + line.quantity,
      0,
    )
    this.cartSubject.next(cart)
    localStorage.setItem('cart', JSON.stringify(cart))
  }

  clearCart() {
    this.cartSubject.next({ lineas: [], totalPrice: 0, totalQuantity: 0 })
    localStorage.removeItem('cart')
  }
}
