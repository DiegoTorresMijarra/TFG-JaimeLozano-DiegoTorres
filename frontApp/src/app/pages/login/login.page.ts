import { Component } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { Router, RouterLink } from '@angular/router'
import { AuthService } from '../../services/auth.service'
import { AppComponent } from '../../app.component'
import { IonicModule, ToastController } from '@ionic/angular'

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, IonicModule],
})
export class LoginPage {
  username: string | undefined
  password: string | undefined
  errorMessage: string | undefined = undefined

  constructor(
    private router: Router,
    private authService: AuthService,
    private appComponent: AppComponent,
    private toastController: ToastController,
  ) {}

  async login() {
    if (this.username && this.password) {
      this.authService.login(this.username, this.password).subscribe({
        next: (response) => {
          this.router.navigate(['/products'])
        },
        error: (err) => {
          this.errorMessage = 'Usuario o contraseña icorrectos'
          console.error('Login error', err)
        },
      })
    } else {
      this.errorMessage = 'Porfavor ingresa ambos campos.'
    }

    if (this.errorMessage) {
      await this.showErrorMessage(this.errorMessage)
      this.errorMessage = undefined
    }
  }

  async showErrorMessage(errorMessage: string) {
    const toast = await this.toastController.create({
      message: errorMessage,
      color: 'danger',
      position: 'top',
      positionAnchor: 'toolbar-header',
      duration: 2000,
    })

    await toast.present()
  }
}
