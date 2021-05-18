import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeistDetailsComponent } from './heist-details.component';

describe('HeistDetailsComponent', () => {
  let component: HeistDetailsComponent;
  let fixture: ComponentFixture<HeistDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeistDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeistDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
