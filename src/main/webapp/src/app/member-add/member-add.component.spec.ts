import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberAddComponent } from './member-add.component';
import {FormBuilder} from '@angular/forms';
import {MemberService} from '../services/member.service';
import {Router} from '@angular/router';
import {
  NbLayoutModule,
  NbThemeModule,
  NbToastrModule,
  NbToastrService
} from '@nebular/theme';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {Component} from '@angular/core';

describe('MemberAddComponent', () => {
  let component: MemberAddComponent;
  let fixture: ComponentFixture<MemberAddComponent>;
  let memberService: MemberService;
  let formBuilder: FormBuilder;
  let router: Router;
  let toastrService: NbToastrService;

  const memberFormGroup = {
    name: '',
    sex: 'M',
    email: '',
    status: '',
    skills: []
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        MemberAddComponent
      ],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        NbThemeModule.forRoot(),
        NbLayoutModule,
        NbToastrModule.forRoot()
      ],
      providers: [FormBuilder],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MemberAddComponent);
    component = fixture.componentInstance;
    memberService = TestBed.inject(MemberService);
    formBuilder = TestBed.inject(FormBuilder);
    router = TestBed.inject(Router);
    toastrService = TestBed.inject(NbToastrService);
    fixture.detectChanges();
  });

  it('should initialize memberFormGroup', () => {
    expect(component.memberFormGroup.value).toEqual(memberFormGroup);
  });

  it('should invalidate the form', () => {
    component.memberFormGroup.controls.name.setValue('');
    component.memberFormGroup.controls.sex.setValue('');
    component.memberFormGroup.controls.email.setValue('mateinmail');
    component.memberFormGroup.controls.status.setValue('');
    expect(component.memberFormGroup.valid).toBeFalsy();
  });

  it('should validate the form', () => {
    component.memberFormGroup.controls.name.setValue('Tokyo');
    component.memberFormGroup.controls.sex.setValue('F');
    component.memberFormGroup.controls.email.setValue('tokyo@ag04.com');
    component.memberFormGroup.controls.status.setValue('AVAILABLE');
    expect(component.memberFormGroup.valid).toBeTruthy();
  });

  it('should save member', () => {
    const spyMember = jest.spyOn(memberService, 'saveMember');
    expect(memberService.saveMember(memberFormGroup)).toBeTruthy();
    expect(spyMember).toHaveBeenCalledWith(memberFormGroup);
  });
});
