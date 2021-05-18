import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MemberAddComponent} from "./member-add/member-add.component";
import {HeistAddComponent} from "./heist-add/heist-add.component";
import {MemberDetailsComponent} from "./member-details/member-details.component";
import {HeistDetailsComponent} from "./heist-details/heist-details.component";
import {HeistEligibleMembersComponent} from "./heist-eligible-members/heist-eligible-members.component";
import {HeistListComponent} from "./heist-list/heist-list.component";

const routes: Routes = [
  { path: 'heists', component: HeistListComponent },
  { path: 'heistEligibleMembers/:id', component: HeistEligibleMembersComponent },
  { path: 'heist/:id', component: HeistDetailsComponent },
  { path: 'addHeist', component: HeistAddComponent },
  { path: 'member/:id', component: MemberDetailsComponent },
  { path: 'addMember', component: MemberAddComponent },
  { path: '', redirectTo: '/heists', pathMatch: 'full' },
  { path: '**', redirectTo: '/heists', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
