import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeistEligibleMembersComponent } from './heist-eligible-members.component';

describe('HeistEligibleMembersComponent', () => {
  let component: HeistEligibleMembersComponent;
  let fixture: ComponentFixture<HeistEligibleMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeistEligibleMembersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeistEligibleMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
