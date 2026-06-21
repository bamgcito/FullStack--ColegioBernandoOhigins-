import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, switchMap, map, catchError, of } from 'rxjs';
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
      `${environment.apiUrl}/bff/ms/auth/login`, { rut, contrasena: password }
    ).pipe(
      switchMap(loginResp => {
        const perfilUrl = this.urlPerfilPorRol(loginResp.nombreRol, loginResp.id);
        if (!perfilUrl) return of({ loginResp, perfil: null as any });
        return this.http.get<any>(
          perfilUrl,
          { headers: { Authorization: `Bearer ${loginResp.token}` } }
        ).pipe(
          map(perfil => ({ loginResp, perfil })),
          catchError(() => of({ loginResp, perfil: null as any }))
        );
      }),
      map(({ loginResp, perfil }) => {
        const authUser: AuthUser = {
          id: loginResp.id,
          rut: loginResp.rut,
          nombre: perfil?.nombre || (loginResp.nombreRol === 'ADMIN' ? 'Administrador' : ''),
          apellido: perfil?.apellido || '',
          rol: loginResp.nombreRol,
          token: loginResp.token
        };
        localStorage.setItem('auth_user', JSON.stringify(authUser));
        this.currentUserSubject.next(authUser);
        return { success: true, message: 'OK' };
      }),
      catchError(error => {
        return of({ success: false, message: error.error?.error || 'RUT o contraseña incorrectos' });
      })
    );
  }

  private urlPerfilPorRol(rol: string, usuarioId: number): string | null {
    switch (rol) {
      case 'PROFESOR': return `${environment.apiUrl}/bff/ms/perfiles/profesores/${usuarioId}`;
      case 'ALUMNO': return `${environment.apiUrl}/bff/ms/perfiles/alumnos/${usuarioId}`;
      case 'APODERADO': return `${environment.apiUrl}/bff/ms/perfiles/apoderados/${usuarioId}`;
      default: return null;
    }
  }

  logout(): void {
    const uid = this.currentUserSubject.value?.id;
    localStorage.removeItem('auth_user');
    localStorage.removeItem('apoderado_pupilo');
    if (uid) localStorage.removeItem(`notif_descartadas_${uid}`);
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
