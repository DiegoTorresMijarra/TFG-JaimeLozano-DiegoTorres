import { Category } from './category.entity'

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
}
export interface ProductSaveDto {
  name: string
  price: number
  stock: number
  gluten: boolean
  categoryId: number
}
