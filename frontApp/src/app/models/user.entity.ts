import { Order } from './order.entity'
import { Address } from './address.entity'

export interface UserResponseDto {
  name: string
  surname: string
  username: string
  email: string
  roles: string[]
  orders: Order[]
  addresses: Address[]
}

export interface UserSignUpRequest {
  name: string
  surname: string
  username: string
  email: string
  password: string
  passwordRepeat: string
}

export interface User {
  id?: string
  name: string
  surname: string
  username: string
  email: string
  password: string
  orders: Order[]
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}
