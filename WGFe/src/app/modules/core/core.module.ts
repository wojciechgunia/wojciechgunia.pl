import { NgModule } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ThemeToggleComponent } from './components/header/theme-toggle/theme-toggle.component';
import { LangSelectComponent } from './components/header/lang-select/lang-select.component';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    ThemeToggleComponent,
    LangSelectComponent,
  ],
  imports: [
    SharedModule,
    NgOptimizedImage,
    RouterLink,
    RouterLinkActive,
    HttpClientModule,
  ],
  exports: [HeaderComponent, FooterComponent],
})
export class CoreModule {}
