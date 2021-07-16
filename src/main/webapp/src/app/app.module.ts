import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import '@angular/common/locales/global/hr';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {
  NbButtonModule, NbCheckboxModule, NbDatepickerModule, NbTimepickerModule,
  NbFormFieldModule,
  NbIconModule,
  NbInputModule,
  NbLayoutModule,
  NbRadioModule, NbSelectModule,
  NbThemeModule, NbListModule, NbCardModule, NbToastrModule, NbTagModule, NbDialogModule, NbDialogRef, NbDialogService
} from '@nebular/theme';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NbEvaIconsModule} from '@nebular/eva-icons';
import {MemberAddComponent} from './member-add/member-add.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { MemberDetailsComponent } from './member-details/member-details.component';
import { HeistAddComponent } from './heist-add/heist-add.component';
import { HeistDetailsComponent } from './heist-details/heist-details.component';
import {ErrorInterceptor} from './interceptors/error.interceptor';
import { HeistEligibleMembersComponent } from './heist-eligible-members/heist-eligible-members.component';
import { HeistListComponent } from './heist-list/heist-list.component';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import { SkillListComponent } from './skill-list/skill-list.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { MemberListComponent } from './member-list/member-list.component';

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
    SkillListComponent,
    NotFoundComponent,
    MemberListComponent
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
    Ng2SmartTableModule,
    NbTagModule,
    NbDialogModule.forRoot()
  ],
  providers: [
    httpInterceptorProviders,
    { provide: LOCALE_ID, useValue: 'en-HR' }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
