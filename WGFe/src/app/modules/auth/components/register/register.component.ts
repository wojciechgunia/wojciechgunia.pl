import { Component, OnDestroy, signal } from '@angular/core';
import { FormService } from '../../../core/services/form.service';
import { FormControl, FormGroup } from '@angular/forms';
import { RegisterForm } from '../../../core/models/forms.model';
import * as AuthActions from '../../store/auth.actions';
import { AppState } from '../../../../store/app.reducer';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectAuthError } from '../../store/auth.selectors';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent implements OnDestroy {
  constructor(
    private formService: FormService,
    private store: Store<AppState>,
  ) {
    this.registerForm = this.formService.initRegisterForm();
    this.errorMsg = this.store.select(selectAuthError);
  }

  errorMsg: Observable<string | null>;
  registerForm: FormGroup<RegisterForm>;

  get controls() {
    return this.registerForm.controls;
  }

  hide = signal(true);
  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }

  onRegister() {
    const { login, name, surname, email, password } =
      this.registerForm.getRawValue();
    this.store.dispatch(
      AuthActions.register({
        registerData: { login, name, surname, email, password },
      }),
    );
  }

  ngOnDestroy(): void {
    this.store.dispatch(AuthActions.clearError());
  }
}
