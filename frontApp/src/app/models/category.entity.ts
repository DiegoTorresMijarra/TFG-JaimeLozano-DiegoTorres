export interface Category {
  id?: number
  name: string
  createdAt: Date
  updatedAt: Date
  deletedAt: Date | null
}

export interface CategoryDto {
  name: string
}
