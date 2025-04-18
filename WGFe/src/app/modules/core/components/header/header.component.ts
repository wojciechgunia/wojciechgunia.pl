import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit {
  selectedValue = 'pl';

  items = [
    {
      name: 'PL',
      value: 'pl',
    },
    {
      name: 'EN',
      value: 'en',
    },
  ];

  get selectedItem() {
    return this.items.find((item) => item.value === this.selectedValue);
  }

  ngOnInit(): void {
    fetch('logo.svg')
      .then((response) => response.text())
      .then((data) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        document.getElementById('svg-logo').innerHTML = data;
      });
  }
}
