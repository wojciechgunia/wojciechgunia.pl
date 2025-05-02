import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginForm, RegisterForm } from '../models/forms.model';
import { equivalentValidator } from '../../shared/validators/equivalent.validator';

@Injectable({
  providedIn: 'root',
})
export class FormService {
  initLoginForm(): FormGroup<LoginForm> {
    return new FormGroup({
      login: new FormControl('', {
        validators: [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(20),
        ],
        nonNullable: true,
      }),
      password: new FormControl('', {
        validators: [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(20),
        ],
        nonNullable: true,
      }),
    });
  }

  initRegisterForm(): FormGroup<RegisterForm> {
    return new FormGroup(
      {
        login: new FormControl('', {
          validators: [
            Validators.required,
            Validators.minLength(4),
            Validators.maxLength(20),
          ],
          nonNullable: true,
        }),
        name: new FormControl('', {
          validators: [
            Validators.required,
            Validators.minLength(2),
            Validators.maxLength(20),
          ],
          nonNullable: true,
        }),
        surname: new FormControl('', {
          validators: [
            Validators.required,
            Validators.minLength(2),
            Validators.maxLength(20),
          ],
          nonNullable: true,
        }),
        email: new FormControl('', {
          validators: [Validators.required, Validators.email],
          nonNullable: true,
        }),
        password: new FormControl('', {
          validators: [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(20),
          ],
          nonNullable: true,
        }),
        repPassword: new FormControl('', {
          validators: [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(20),
          ],
          nonNullable: true,
        }),
      },
      { validators: [equivalentValidator('password', 'repPassword')] },
    );
  }

  getErrorMessage(control: FormControl): string {
    if (control.hasError('required')) {
      return 'Pole jest wymagane';
    } else if (control.hasError('minlength')) {
      return `Minimalna długość: ${control.errors?.['minlength']?.requiredLength}`;
    } else if (control.hasError('maxlength')) {
      return `Maksymalna długość: ${control.errors?.['maxlength']?.requiredLength}`;
    } else if (control.hasError('email')) {
      return 'Niepoprawny adres email';
    } else if (control.hasError('passwordNotEqual')) {
      return 'Hasła muszą być takie same';
    }
    return '';
  }
}
