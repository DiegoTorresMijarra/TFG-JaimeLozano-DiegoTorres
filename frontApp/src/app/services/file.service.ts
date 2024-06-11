import { Component } from '@angular/core'
import { Camera, CameraResultType, CameraSource } from '@capacitor/camera'
import { HttpClient } from '@angular/common/http'

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss'],
  standalone: true,
})
export class UploadComponent {
  selectedImage: any

  constructor(private http: HttpClient) {}

  async takePicture() {
    const image = await Camera.getPhoto({
      quality: 90,
      allowEditing: false,
      resultType: CameraResultType.Base64,
      source: CameraSource.Camera,
    })

    this.selectedImage = `data:image/jpeg;base64,${image.base64String}`
    this.uploadImage(image.format, image.base64String)
  }

  uploadImage(format: string, base64String?: string) {
    const formData = new FormData()
    formData.append('nombre', 'Nombre del producto')
    formData.append(
      'imagen',
      this.base64ToFile(`imagen.${format}`, base64String),
    )

    this.http.post('/api/productos/upload', formData).subscribe((response) => {
      console.log('Upload successful:', response)
    })
  }

  base64ToFile(fileName: string, base64String?: string): File {
    if (base64String) {
      const byteString = atob(base64String)
      const arrayBuffer = new ArrayBuffer(byteString.length)
      const intArray = new Uint8Array(arrayBuffer)
      for (let i = 0; i < byteString.length; i++) {
        intArray[i] = byteString.charCodeAt(i)
      }
      return new File([intArray], fileName, { type: 'image/jpeg' })
    } else throw new Error()
  }
}
