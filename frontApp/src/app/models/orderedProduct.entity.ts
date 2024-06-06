import { Product } from './product.entity'

export interface OrderedProduct {
  quantity: number
  productId: number
  productPrice: number
  totalPrice: number
  product?: Product
}
