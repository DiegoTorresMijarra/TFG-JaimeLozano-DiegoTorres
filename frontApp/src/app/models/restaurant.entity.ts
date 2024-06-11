export interface Restaurant {
  id?: number
  name: string
  phone: string
  address: string
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
export interface RestaurantDto {
  name: string
  phone: string
  address: string
}
