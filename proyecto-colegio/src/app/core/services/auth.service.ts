import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, map, catchError, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface AuthUser {
  id: number;
  rut: string;
  nombre: string;
  apellido: string;
  rol: 'ADMIN' | 'PROFESOR' | 'ALUMNO' | 'APODERADO';
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<AuthUser | null>(this.loadFromStorage());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private router: Router, private http: HttpClient) { }

  private loadFromStorage(): AuthUser | null {
    try {
      const s = localStorage.getItem('auth_user');
      return s ? JSON.parse(s) : null;
    } catch { return null; }
  }

  get currentUser(): AuthUser | null { return this.currentUserSubject.value; }
  get isLoggedIn(): boolean { return !!this.currentUser; }
  get rol(): string { return this.currentUser?.rol || ''; }

  login(rut: string, password: string): Observable<{ success: boolean; message: string }> {
    return this.http.post<any>(
      `${environment.apiUrl}/bff/auth/login`, { rut, contrasena: password }
    ).pipe(
      map(response => {
        const authUser: AuthUser = {
          id: response.id,
          rut: response.rut,
          nombre: response.nombre,
          apellido: response.apellido,
          rol: response.nombreRol,
          token: response.token
        };
        localStorage.setItem('auth_user', JSON.stringify(authUser));
        this.currentUserSubject.next(authUser);
        return { success: true, message: 'OK' };
      }),
      catchError(error => {
        return of({ success: false, message: error.error?.message || 'RUT o contraseña incorrectos' });
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login'], { replaceUrl: true });
  }

  redirectByRole(): void {
    const opts = { replaceUrl: true };
    switch (this.currentUser?.rol) {
      case 'ADMIN': this.router.navigate(['/admin/dashboard'], opts); break;
      case 'PROFESOR': this.router.navigate(['/profesor/dashboard'], opts); break;
      case 'ALUMNO': this.router.navigate(['/alumno/dashboard'], opts); break;
      case 'APODERADO': this.router.navigate(['/apoderado/dashboard'], opts); break;
      default: this.router.navigate(['/login']);
    }
  }
}