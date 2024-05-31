import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput, IonItem, IonLabel,
  IonNote,
  IonText,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonText, IonNote, IonInput, IonLabel, IonItem, ReactiveFormsModule]
})
export class RegisterPage {

  registerForm: FormGroup;

  constructor(private router: Router,private fb: FormBuilder, private authService: AuthService) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required]],
      surname: ['', [Validators.required]],
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(5)]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    return form.get('password')?.value === form.get('passwordRepeat')?.value
      ? null : { 'mismatch': true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: response => {
          console.log('Registration successful:', response);
          this.router.navigate(['/login']);
        },
        error: error => {
          console.error('Registration error:', error);
          // Manejo de errores específicos
          if (error.status === 400) {
            alert('Error de validación: algunos campos son inválidos.');
          } else if (error.status === 409) {
            alert('Error: el usuario ya existe.');
          } else {
            alert('Error desconocido, por favor intente más tarde.');
          }
        }
      });
    }
  }

}
