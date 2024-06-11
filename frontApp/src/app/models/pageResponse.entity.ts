export class PageResponse<T> {
  content: T[]
  totalPages: number
  totalElements: number
  pageSize: number
  pageNumber: number
  totalPageElements: number
  empty: boolean
  first: boolean
  last: boolean
  sortBy: string
  direction: string

  constructor(data: Partial<PageResponse<T>>) {
    this.content = data.content || []
    this.totalPages = data.totalPages ?? 0
    this.totalElements = data.totalElements ?? 0
    this.pageSize = data.pageSize ?? 0
    this.pageNumber = data.pageNumber ?? 0
    this.totalPageElements = data.totalPageElements ?? 0
    this.empty = data.empty || false
    this.first = data.first || false
    this.last = data.last || false
    this.sortBy = data.sortBy ?? ''
    this.direction = data.direction ?? ''
  }
}
