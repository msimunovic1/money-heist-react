import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MemberAddComponent} from "./member-add/member-add.component";
import {HeistAddComponent} from "./heist-add/heist-add.component";

const routes: Routes = [
  { path: 'addHeist', component: HeistAddComponent },
  { path: 'addMember', component: MemberAddComponent },
  { path: '', redirectTo: '/', pathMatch: 'full' },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
