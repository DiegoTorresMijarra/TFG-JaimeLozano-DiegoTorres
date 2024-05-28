import {Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {Observable} from 'rxjs'

export class TrabajoModel {
  id: number
  titulo: string
  logo: string
  nota: string
  resumen: string
  descripcion: string
  images: string[]
  url: string | null | undefined
  code: string | null | undefined

  constructor(
    id: number,
    titulo: string,
    logo: string,
    nota: string,
    resumen: string,
    descripcion: string,
    images: string[],
    url: string | null | undefined,
    code: string | null | undefined
  ) {
    this.id = id
    this.titulo = titulo
    this.logo = logo
    this.nota = nota
    this.resumen = resumen
    this.descripcion = descripcion
    this.images = images
    this.url = url
    this.code = code
  }
}

@Injectable({
  providedIn: 'root',
})
export class TrabajosService {
  constructor(private http: HttpClient) {
  }

  getTrabajos(): Observable<TrabajoModel[]> {
    return this.http.get<TrabajoModel[]>('assets/trabajos/data/trabajos.json')
  }
}
