import { OrderedProduct } from './orderedProduct.entity'

export interface Order {
  id?: string // Usaremos string para ObjectId
  userId: string // UUID como string
  restaurantId: number
  addressesId: string // UUID como string
  orderedProducts: OrderedProduct[]
  totalPrice: number
  totalQuantityProducts: number
  isPaid: boolean
  state: OrderState
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  restaurantName?: string
}

export interface OrderDto {
  userId?: string
  restaurantId: number
  addressesId: string
  orderedProducts: OrderedProduct[]
}

export enum OrderState {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  DELIVERED = 'DELIVERED',
  CANCELED = 'CANCELED',
  DELETED = 'DELETED',
}
