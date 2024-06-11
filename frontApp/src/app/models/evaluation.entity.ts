import { Product } from './product.entity'

export interface Evaluation {
  id?: number
  value: number
  comment: string
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  product: Product
  //user: UserEntity
}
export interface EvaluationDto {
  value: number
  comment: string
  productId: number
}
export interface EvaluationResponseDto {
  value: number
  comment: string
  productId: number
  userName: string
  createdAt: Date
}
