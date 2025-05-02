import { Component } from '@angular/core';

@Component({
  selector: 'app-lang-select',
  standalone: false,
  templateUrl: './lang-select.component.html',
  styleUrl: './lang-select.component.scss',
})
export class LangSelectComponent {
  dropdown = false;
  lang = 'pl';

  changeLang(lang: string) {
    this.lang = lang;
    this.dropdown = false;
  }
}
