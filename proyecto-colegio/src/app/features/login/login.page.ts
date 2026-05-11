import { Component, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  eyeOutline, eyeOffOutline, alertCircleOutline, logInOutline,
  arrowBackOutline, shieldCheckmarkOutline, personOutline,
  schoolOutline, peopleOutline
} from 'ionicons/icons';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, IonContent, IonIcon],
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  host: { class: 'ion-page' }
})
export class LoginPage {
  rut = '';
  password = '';
  errorMsg = '';
  loading = false;
  showPassword = false;

  constructor(private authService: AuthService, private ngZone: NgZone) {
    addIcons({
      eyeOutline, eyeOffOutline, alertCircleOutline, logInOutline,
      arrowBackOutline, shieldCheckmarkOutline, personOutline,
      schoolOutline, peopleOutline
    });
  }

  login(): void {
    this.errorMsg = '';
    if (!this.rut || !this.password) {
      this.errorMsg = 'Por favor ingresa tu RUT y contraseña.';
      return;
    }
    this.loading = true;
    this.authService.login(this.rut.trim(), this.password).subscribe(result => {
      this.loading = false;
      if (result.success) {
        this.ngZone.run(() => this.authService.redirectByRole());
      } else {
        this.errorMsg = result.message;
      }
    });
  }

  fillDemo(rut: string, password: string): void {
    this.rut = rut;
    this.password = password;
    this.errorMsg = '';
  }
}