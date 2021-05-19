import { Component, OnInit } from '@angular/core';
import {HeistService} from "../services/heist.service";
import {MembersEligibleForHeist} from "../models/members-eligible-for-heist";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {HeistMember} from "../models/heist-member";
import {IHeistMembers} from "../models/i-heist-members";
import {NbToastrService} from "@nebular/theme";

@Component({
  selector: 'app-heist-eligible-members',
  templateUrl: './heist-eligible-members.component.html',
  styleUrls: ['./heist-eligible-members.component.css']
})
export class HeistEligibleMembersComponent implements OnInit {

  membersEligibleForHeist: MembersEligibleForHeist = new MembersEligibleForHeist();
  eligibleMembers: HeistMember[] = [];
  heistId: number = 0;

  heistMembers: string[] = [];
  iHeistMembers: IHeistMembers = new IHeistMembers();

  constructor(private heistService: HeistService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    // get id from url param
    this.route.paramMap.subscribe(() => {
      // @ts-ignore
      this.heistId = +this.route.snapshot.paramMap.get('id');

      this.heistService.getMembersEligibleForHeist(this.heistId).subscribe(
        data => this.membersEligibleForHeist = data,
        () => {},
        () => {
          if(this.membersEligibleForHeist.members !== undefined) {
            this.eligibleMembers = this.membersEligibleForHeist.members;
          }
        }
      );
    });

  }

  addMemberToHeist(name: string, index: number) {
    // remove member from eligible members array
    this.eligibleMembers.splice(index, 1);
    // add member to heist
    this.heistMembers.push(name);
    // remap heist member array
    this.iHeistMembers.members = this.heistMembers;
  }

  confirmMembers() {
    this.heistService.saveHeistMembers(this.heistId, this.iHeistMembers).subscribe(
      () => {
        this.router.navigateByUrl(`/heist/${this.heistId}`)
      }
    );
  }
}
