import {Component, OnInit} from '@angular/core';
import {Member} from "../models/member";
import {MemberService} from "../services/member.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.css']
})
export class MemberDetailsComponent implements OnInit {

  member: Member = new Member();
  memberId: number = 0;

  constructor(private memberService: MemberService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {

    // get id from url param
    this.route.paramMap.subscribe(() => {
      this.handleMemberDetails();
    });

  }

  handleMemberDetails() {

    // @ts-ignore
    this.memberId = +this.route.snapshot.paramMap.get('id');

    // get heist details from service
    this.memberService.getMember(this.memberId).subscribe(
      data => this.member = data
    );

  }
}
