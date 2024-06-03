export class ProductFiltersDto {
  page: number = 0
  size: number = 5
  sortBy: string = 'id'
  direction: string = 'asc'
  name?: string
  stockMax?: number
  stockMin?: number
  priceMax?: number
  priceMin?: number
  gluten?: boolean
  categoryId?: number

  constructor(init?: Partial<ProductFiltersDto>) {
    Object.assign(this, init)
  }
}
