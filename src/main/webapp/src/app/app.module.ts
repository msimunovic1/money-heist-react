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
  NbThemeModule, NbListModule, NbCardModule, NbToastrModule
} from "@nebular/theme";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {MemberAddComponent} from './member-add/member-add.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { MemberDetailsComponent } from './member-details/member-details.component';
import { HeistAddComponent } from './heist-add/heist-add.component';
import { HeistDetailsComponent } from './heist-details/heist-details.component';
import {ErrorInterceptor} from "./interceptors/error.interceptor";
import { HeistEligibleMembersComponent } from './heist-eligible-members/heist-eligible-members.component';
import { HeistListComponent } from './heist-list/heist-list.component';
import {Ng2SmartTableModule} from "ng2-smart-table";
import { HeistSkillsComponent } from './heist-skills/heist-skills.component';

export const httpInterceptorProviders = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptor,
    multi: true
  }
];

@NgModule({
  declarations: [
    AppComponent,
    MemberAddComponent,
    MemberDetailsComponent,
    HeistAddComponent,
    HeistDetailsComponent,
    HeistEligibleMembersComponent,
    HeistListComponent,
    HeistSkillsComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    NbLayoutModule,
    BrowserAnimationsModule,
    NbThemeModule.forRoot({name: 'cosmic'}),
    NbToastrModule.forRoot(),
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
    NbTimepickerModule.forRoot(),
    NbListModule,
    NbCardModule,
    Ng2SmartTableModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
