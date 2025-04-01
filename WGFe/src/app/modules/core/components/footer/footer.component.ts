import { Component } from '@angular/core';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: false,
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.scss',
})
export class FooterComponent {
  date = formatDate(Date.now(), 'YYYY', 'en');
}
