import { Component } from '@angular/core';
import { IonContent, IonIcon } from '@ionic/angular/standalone';
import { Router } from '@angular/router';
import { addIcons } from 'ionicons';
import { logInOutline } from 'ionicons/icons';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [IonContent, IonIcon],
  templateUrl: './landing.page.html',
  styleUrls: ['./landing.page.scss'],
  host: { class: 'ion-page' }
})
export class LandingPage {
  constructor(private router: Router) { 
    addIcons({ logInOutline }); 
  }

  irAlLogin() {
    this.router.navigate(['/login']);
  }
}