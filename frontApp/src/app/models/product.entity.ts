import { Category } from './category.entity'
import { Evaluation } from './evaluation.entity'

export interface Product {
  id?: number
  name: string
  price: number
  stock: number
  gluten: boolean
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  category: Category
  averageRating: number
  // evaluations: Evaluation[]
}

export interface ProductSaveDto {
  name: string
  price: number
  stock: number
  gluten: boolean
  categoryId: number
}
