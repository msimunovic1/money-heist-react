import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {
  NbButtonModule, NbCheckboxModule, NbDatepickerModule, NbTimepickerModule,
  NbFormFieldModule,
  NbIconModule,
  NbInputModule,
  NbLayoutModule,
  NbRadioModule, NbSelectModule,
  NbThemeModule
} from "@nebular/theme";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {MemberAddComponent} from './member-add/member-add.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { MemberDetailsComponent } from './member-details/member-details.component';
import { HeistAddComponent } from './heist-add/heist-add.component';
import { HeistDetailsComponent } from './heist-details/heist-details.component';

@NgModule({
  declarations: [
    AppComponent,
    MemberAddComponent,
    MemberDetailsComponent,
    HeistAddComponent,
    HeistDetailsComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    NbLayoutModule,
    BrowserAnimationsModule,
    NbThemeModule.forRoot({name: 'cosmic'}),
    NbEvaIconsModule,
    NbInputModule,
    NbFormFieldModule,
    NbIconModule,
    NbRadioModule,
    FormsModule,
    NbButtonModule,
    NbSelectModule,
    NbCheckboxModule,
    NbDatepickerModule.forRoot(),
    NbTimepickerModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
