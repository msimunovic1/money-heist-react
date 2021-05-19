import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeistSkillsComponent } from './heist-skills.component';

describe('HeistSkillsComponent', () => {
  let component: HeistSkillsComponent;
  let fixture: ComponentFixture<HeistSkillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeistSkillsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeistSkillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
