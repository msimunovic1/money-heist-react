import {Component, OnInit} from '@angular/core';
import {HeistService} from "../services/heist.service";
import {MembersEligibleForHeist} from "../models/members-eligible-for-heist";
import {ActivatedRoute, Router} from "@angular/router";
import {HeistMember} from "../models/heist-member";
import {IHeistMembers} from "../models/i-heist-members";

@Component({
  selector: 'app-heist-eligible-members',
  templateUrl: './heist-eligible-members.component.html',
  styleUrls: ['./heist-eligible-members.component.css']
})
export class HeistEligibleMembersComponent implements OnInit {

  membersEligibleForHeist: MembersEligibleForHeist = new MembersEligibleForHeist();
  eligibleMembers: string[] = [];
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
          this.membersEligibleForHeist.members?.map(member => {
            if (member.name != null) {
              this.eligibleMembers.push(member.name)
            }
          })
        }
      );
    });

  }

  addMemberToHeist(member: string, index: number) {
    // remove member from eligible members array
    this.eligibleMembers.splice(index, 1);
    // add member to heist
    this.heistMembers.push(member);
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

  removeMemberFromHeist(member: string, index: number) {
    // remove member from eligible members array
    this.heistMembers.splice(index, 1);
    // add member to heist
    this.eligibleMembers.push(member)
    // remap heist member array
    this.iHeistMembers.members = this.heistMembers;
  }
}
