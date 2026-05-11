import { bootstrapApplication } from '@angular/platform-browser';
import { RouteReuseStrategy, provideRouter, withComponentInputBinding } from '@angular/router';
import { IonicRouteStrategy, provideIonicAngular } from '@ionic/angular/standalone';
import { provideHttpClient, withInterceptors, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  try {
    const user = JSON.parse(localStorage.getItem('auth_user') || 'null');
    if (user?.token) {
      req = req.clone({
        setHeaders: { Authorization: `Bearer ${user.token}` }
      });
    }
  } catch { }
  return next(req);
}

bootstrapApplication(AppComponent, {
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    provideIonicAngular({ mode: 'md' }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
});