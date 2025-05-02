import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-theme-toggle',
  standalone: false,
  templateUrl: './theme-toggle.component.html',
  styleUrl: './theme-toggle.component.scss',
})
export class ThemeToggleComponent implements OnInit {
  isDarkMode = true;

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;

    if (!this.isDarkMode) {
      document.body.classList.remove('dark-theme');
      document.body.classList.add('light-theme');
    } else {
      document.body.classList.remove('light-theme');
      document.body.classList.add('dark-theme');
    }
  }

  ngOnInit(): void {
    document.body.classList.add('dark-theme');
  }
}
