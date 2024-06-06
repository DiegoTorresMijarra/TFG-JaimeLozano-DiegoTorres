export interface Notification {
  entity: string
  type: string
  data: OrderData
  createdAt: string
}

export interface OrderData {
  orderId: string
  restaurantId: string
  clientId: string
  addressId: string
  createdAt: string
  updatedAt: string
}
