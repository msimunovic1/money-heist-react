import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberDetailsComponent } from './member-details.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MemberService} from '../services/member.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NbDialogModule, NbDialogService, NbLayoutModule, NbThemeModule, NbToastrModule, NbToastrService} from '@nebular/theme';
import {RouterTestingModule} from '@angular/router/testing';
import {Member} from '../models/member';
import {of} from 'rxjs';

describe('MemberDetailsComponent', () => {
  let component: MemberDetailsComponent;
  let fixture: ComponentFixture<MemberDetailsComponent>;
  let memberService: MemberService;
  let activatedRoute: ActivatedRoute;
  let router: Router;
  let toastrService: NbToastrService;
  let dialogService: NbDialogService;

  const memberId = 1;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MemberDetailsComponent
      ],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        NbThemeModule.forRoot(),
        NbLayoutModule,
        NbToastrModule.forRoot(),
        NbDialogModule.forRoot()
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberDetailsComponent);
    component = fixture.componentInstance;
    memberService = TestBed.inject(MemberService);
    activatedRoute = TestBed.inject(ActivatedRoute);
    router = TestBed.inject(Router);
    toastrService = TestBed.inject(NbToastrService);
    dialogService = TestBed.inject(NbDialogService);
    fixture.detectChanges();
  });

  it('should fetch member details', () => {
    const member = new Member();
    member.name = 'Tokyo';
    member.sex = 'F';
    member.email = 'tokyo@ag04.com';
    member.skills = [
      {
        name: 'combat',
        level: '**'
      }
    ];
    member.mainSkill = 'combat';
    member.status = 'AVAILABLE';
    const spyMember = jest.spyOn(memberService, 'getMember').mockReturnValue(of(member));
    expect(memberService.getMember(memberId)).toBeTruthy();
    expect(spyMember).toHaveBeenCalledWith(memberId);
  });

});
