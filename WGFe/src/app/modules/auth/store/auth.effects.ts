import { AuthService } from '../../core/services/auth.service';
import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap } from 'rxjs';
import * as AuthActions from './auth.actions';
import { Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';
@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      switchMap((action) => {
        return this.authService.login(action.loginData).pipe(
          map((user) => {
            if (user.login) {
              this.router.navigate(['/']);
              this.notifierService.notify('success', `Witaj ${user.login}`);
              return AuthActions.loginSuccess({ user: { ...user } });
            } else {
              return AuthActions.loginFailure({ error: 'error login' });
            }
          }),
          catchError((err) => {
            return of(AuthActions.loginFailure({ error: err.error.message }));
          }),
        );
      }),
    ),
  );

  logout$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.logout),
      switchMap(() => {
        return this.authService.logout().pipe(
          map(() => {
            this.router.navigate(['/logowanie']);
            this.notifierService.notify('success', `Wylogowano się`);
            return AuthActions.logoutSuccess();
          }),
          catchError((err) => {
            this.notifierService.notify('warning', err);
            return of(AuthActions.logoutFailure());
          }),
        );
      }),
    ),
  );

  autoLogin$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.autoLogin),
      switchMap(() => {
        return this.authService.autoLogin().pipe(
          map((user) => {
            return AuthActions.autoLoginSuccess({ user: { ...user } });
          }),
          catchError(() => of(AuthActions.autoLoginFailure())),
        );
      }),
    ),
  );

  register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      switchMap((action) => {
        return this.authService.register(action.registerData).pipe(
          map(() => {
            this.router.navigate(['/admin']);
            this.notifierService.notify(
              'success',
              'Rejestracja zakończona sukcesem!',
            );
            return AuthActions.registerSuccess();
          }),
          catchError((err) => {
            return of(
              AuthActions.registerFailure({ error: err.error.message }),
            );
          }),
        );
      }),
    ),
  );

  constructor(
    private authService: AuthService,
    private router: Router,
    private notifierService: NotifierService,
  ) {}
}
