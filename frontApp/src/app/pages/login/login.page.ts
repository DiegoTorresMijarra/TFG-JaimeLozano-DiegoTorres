import { Component } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { Router, RouterLink } from '@angular/router'
import { AuthService } from '../../services/auth.service'
import { AppComponent } from '../../app.component'
import { IonicModule } from '@ionic/angular'

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
  errorMessage: string | undefined

  constructor(
    private router: Router,
    private authService: AuthService,
    private appComponent: AppComponent,
  ) {}

  login() {
    if (this.username && this.password) {
      this.authService.login(this.username, this.password).subscribe({
        next: () => {
          this.router.navigate(['/products'])
        },
        error: (err) => {
          this.errorMessage =
            'Login fallido. Por favor revisa sus credenciales.'
          console.error('Login error', err)
        },
      })
    } else {
      this.errorMessage = 'Porfavor ingresa ambos campos.'
    }
  }
}
