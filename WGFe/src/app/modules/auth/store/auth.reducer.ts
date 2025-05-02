/* eslint-disable @ngrx/on-function-explicit-return-type */
import { Action, createReducer, on } from '@ngrx/store';
import { User } from '../../core/models/auth.model';
import * as AuthActions from './auth.actions';

export interface AuthState {
  user: User | null;
  loading: boolean;
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  loading: false,
  error: null,
};

const _authreducer = createReducer(
  initialState,
  on(AuthActions.login, AuthActions.register, (state, action) => ({
    ...state,
    loading: true,
  })),
  on(AuthActions.loginSuccess, (state, action) => ({
    ...state,
    loading: false,
    user: new User(action.user.login, action.user.email, action.user.role),
    error: null,
  })),
  on(
    AuthActions.loginFailure,
    AuthActions.registerFailure,
    (state, action) => ({
      ...state,
      loading: false,
      error: action.error,
    }),
  ),
  on(
    AuthActions.autoLogin,
    AuthActions.autoLoginFailure,
    AuthActions.logout,
    AuthActions.logoutFailure,
    (state, action) => ({
      ...state,
    }),
  ),
  on(AuthActions.autoLoginSuccess, (state, action) => ({
    ...state,
    user: new User(action.user.login, action.user.email, action.user.role),
  })),
  on(AuthActions.logoutSuccess, (state, action) => ({
    ...state,
    user: null,
    error: null,
  })),
  on(AuthActions.registerSuccess, (state, action) => ({
    ...state,
    loading: false,
    error: null,
  })),
  on(AuthActions.clearError, (state, action) => ({
    ...state,
    error: null,
  })),
);

export function authReducer(state: AuthState | undefined, action: Action) {
  return _authreducer(state, action);
}
