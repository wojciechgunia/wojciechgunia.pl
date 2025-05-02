import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StoreModule } from '@ngrx/store';
import { CoreModule } from './modules/core/core.module';
import { AuthModule } from './modules/auth/auth.module';
import { authReducer } from './modules/auth/store/auth.reducer';
import { EffectsModule } from '@ngrx/effects';
import { NotifierModule, NotifierOptions } from 'angular-notifier';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthEffects } from './modules/auth/store/auth.effects';

const customNotifierOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: 'right',
      distance: 12,
    },
    vertical: {
      position: 'top',
      distance: 90,
      gap: 10,
    },
  },
  theme: 'material',
};

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    StoreModule.forRoot({ auth: authReducer }),
    AuthModule,
    CoreModule,
    BrowserAnimationsModule,
    EffectsModule.forRoot([AuthEffects]),
    NotifierModule.withConfig(customNotifierOptions),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
