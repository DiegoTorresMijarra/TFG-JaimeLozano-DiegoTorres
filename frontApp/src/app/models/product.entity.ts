import { Category } from './category.entity'
import { Evaluation } from './evaluation.entity'

export interface Product {
  id: number
  name: string
  price: number
  stock: number
  gluten: boolean
  image: Uint8Array
  imageExtension: string
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
  category: Category
  averageRating: number
  priceOffer?: number
  // evaluations: Evaluation[]
}

export interface ProductSaveDto {
  name: string
  price: number
  stock: number
  gluten: boolean
  categoryId: number
}

export function getProductUrl(product: Product): string {
  return product.image && product.imageExtension
    ? 'data:' + product.imageExtension + ';base64,' + product.image
    : '/assets/products/default.jpg'
}

export const ValidImageExtensions: Set<string> = new Set([
  'image/jpeg',
  'image/jpg',
  'image/png',
  'image/gif',
])
