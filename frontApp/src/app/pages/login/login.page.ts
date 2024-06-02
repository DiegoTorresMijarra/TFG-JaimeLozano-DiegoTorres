import { Component } from '@angular/core'
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms'
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput,
  IonItem,
  IonLabel,
  IonTitle,
  IonToolbar,
} from '@ionic/angular/standalone'
import {Router, RouterLink} from '@angular/router'
import { AuthService } from '../../services/auth.service'
import { AppComponent } from '../../app.component'

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    CommonModule,
    FormsModule,
    IonItem,
    IonLabel,
    IonInput,
    IonButton,
    RouterLink,
  ],
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
