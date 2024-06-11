import { Product } from './product.entity'

export interface CartLine {
  product: Product
  price: number
  quantity: number
  subtotal: number
}

export interface Cart {
  lineas: CartLine[]
  totalPrice: number
  totalQuantity: number
}
