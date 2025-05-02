import { Component, signal } from '@angular/core';
import { FormService } from '../../../core/services/form.service';
import { LoginForm } from '../../../core/models/forms.model';
import { FormControl, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../store/app.reducer';
import * as AuthActions from '../../store/auth.actions';
import { OnDestroy } from '@angular/core';
import { selectAuthError } from '../../store/auth.selectors';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnDestroy {
  constructor(
    private formService: FormService,
    private store: Store<AppState>,
  ) {
    this.loginForm = this.formService.initLoginForm();
    this.errorMsg = this.store.select(selectAuthError);
  }

  errorMsg: Observable<string | null>;

  loginForm: FormGroup<LoginForm>;

  get controls() {
    return this.loginForm.controls;
  }

  hide = signal(true);
  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }

  onLogin() {
    this.store.dispatch(
      AuthActions.login({ loginData: this.loginForm.getRawValue() }),
    );
  }

  ngOnDestroy(): void {
    this.store.dispatch(AuthActions.clearError());
  }
}
