import { Pipe, PipeTransform } from '@angular/core'

@Pipe({
  name: 'paginate',
  standalone: true,
})
export class PaginatePipe implements PipeTransform {
  transform(array: any[], page: number, pageSize: number): any[] {
    if (!array.length) return []
    const start = (page - 1) * pageSize
    return array.slice(start, start + pageSize)
  }
}
