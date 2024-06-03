export class ProductFiltersDtoEntity {
  page: number = 0
  size: number = 10
  sortBy: string = 'id'
  direction: string = 'asc'
  name?: string
  stockMax?: number
  stockMin?: number
  priceMax?: number
  priceMin?: number
  gluten?: boolean
  categoryId?: number

  constructor(init?: Partial<ProductFiltersDtoEntity>) {
    Object.assign(this, init)
  }
}
