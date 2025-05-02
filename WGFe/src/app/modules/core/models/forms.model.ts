import { FormControl } from '@angular/forms';

export interface LoginForm {
  login: FormControl<string>;
  password: FormControl<string>;
}

export interface RegisterForm {
  login: FormControl<string>;
  name: FormControl<string>;
  surname: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
  repPassword: FormControl<string>;
}
