import { Routes } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/auth.service';

const guard = (roles: string[]) => () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!auth.isLoggedIn) { router.navigate(['/login']); return false; }
  if (roles.includes(auth.rol)) return true;
  auth.redirectByRole();
  return false;
};

export const routes: Routes = [
  { path: '', loadComponent: () => import('./features/landing/landing.page').then(m => m.LandingPage) },
  { path: 'login', loadComponent: () => import('./features/login/login.page').then(m => m.LoginPage) },

  { path: 'admin/dashboard',    canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/dashboard/admin-dashboard.page').then(m => m.AdminDashboardPage) },
  { path: 'admin/usuarios',     canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/usuarios/admin-usuarios.page').then(m => m.AdminUsuariosPage) },
  { path: 'admin/cursos',       canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/cursos/admin-cursos.page').then(m => m.AdminCursosPage) },
  { path: 'admin/asignaturas',  canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/asignaturas/admin-asignaturas.page').then(m => m.AdminAsignaturasPage) },
  { path: 'admin/asignaciones', canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/asignaciones/admin-asignaciones.page').then(m => m.AdminAsignacionesPage) },
  { path: 'admin/evaluaciones', canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/evaluaciones/admin-evaluaciones.page').then(m => m.AdminEvaluacionesPage) },
  { path: 'admin/notas',        canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/notas/admin-notas.page').then(m => m.AdminNotasPage) },
  { path: 'admin/asistencia',   canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/asistencia/admin-asistencia.page').then(m => m.AdminAsistenciaPage) },
  { path: 'admin/anotaciones',  canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/admin/anotaciones/admin-anotaciones.page').then(m => m.AdminAnotacionesPage) },
  { path: 'admin/comunicaciones', canActivate: [guard(['ADMIN'])], loadComponent: () => import('./features/shared-pages/comunicaciones/comunicaciones.page').then(m => m.ComunicacionesPage) },

  { path: 'profesor/dashboard',    canActivate: [guard(['PROFESOR'])], loadComponent: () => import('./features/profesor/dashboard/profesor-dashboard.page').then(m => m.ProfesorDashboardPage) },
  { path: 'profesor/mis-cursos',   canActivate: [guard(['PROFESOR'])], loadComponent: () => import('./features/profesor/mis-cursos/profesor-mis-cursos.page').then(m => m.ProfesorMisCursosPage) },
  { path: 'profesor/asistencia',   canActivate: [guard(['PROFESOR'])], loadComponent: () => import('./features/profesor/asistencia/profesor-asistencia.page').then(m => m.ProfesorAsistenciaPage) },
  { path: 'profesor/notas',        canActivate: [guard(['PROFESOR'])], loadComponent: () => import('./features/profesor/notas/profesor-notas.page').then(m => m.ProfesorNotasPage) },
  { path: 'profesor/anotaciones',  canActivate: [guard(['PROFESOR'])], loadComponent: () => import('./features/profesor/anotaciones/profesor-anotaciones.page').then(m => m.ProfesorAnotacionesPage) },
  { path: 'profesor/comunicaciones', canActivate: [guard(['PROFESOR'])], loadComponent: () => import('./features/shared-pages/comunicaciones/comunicaciones.page').then(m => m.ComunicacionesPage) },

  { path: 'alumno/dashboard',    canActivate: [guard(['ALUMNO'])], loadComponent: () => import('./features/alumno/dashboard/alumno-dashboard.page').then(m => m.AlumnoDashboardPage) },
  { path: 'alumno/notas',        canActivate: [guard(['ALUMNO'])], loadComponent: () => import('./features/alumno/notas/alumno-notas.page').then(m => m.AlumnoNotasPage) },
  { path: 'alumno/asistencia',   canActivate: [guard(['ALUMNO'])], loadComponent: () => import('./features/alumno/asistencia/alumno-asistencia.page').then(m => m.AlumnoAsistenciaPage) },
  { path: 'alumno/anotaciones',  canActivate: [guard(['ALUMNO'])], loadComponent: () => import('./features/alumno/anotaciones/alumno-anotaciones.page').then(m => m.AlumnoAnotacionesPage) },
  { path: 'alumno/comunicaciones', canActivate: [guard(['ALUMNO'])], loadComponent: () => import('./features/shared-pages/comunicaciones/comunicaciones.page').then(m => m.ComunicacionesPage) },

  { path: 'apoderado/dashboard',   canActivate: [guard(['APODERADO'])], loadComponent: () => import('./features/apoderado/dashboard/apoderado-dashboard.page').then(m => m.ApoderadoDashboardPage) },
  { path: 'apoderado/notas',       canActivate: [guard(['APODERADO'])], loadComponent: () => import('./features/apoderado/notas/apoderado-notas.page').then(m => m.ApoderadoNotasPage) },
  { path: 'apoderado/asistencia',  canActivate: [guard(['APODERADO'])], loadComponent: () => import('./features/apoderado/asistencia/apoderado-asistencia.page').then(m => m.ApoderadoAsistenciaPage) },
  { path: 'apoderado/anotaciones', canActivate: [guard(['APODERADO'])], loadComponent: () => import('./features/apoderado/anotaciones/apoderado-anotaciones.page').then(m => m.ApoderadoAnotacionesPage) },
  { path: 'apoderado/comunicaciones', canActivate: [guard(['APODERADO'])], loadComponent: () => import('./features/shared-pages/comunicaciones/comunicaciones.page').then(m => m.ComunicacionesPage) },

  { path: '**', redirectTo: '' }
];