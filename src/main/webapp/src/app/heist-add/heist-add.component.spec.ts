import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeistAddComponent } from './heist-add.component';

describe('HeistAddComponent', () => {
  let component: HeistAddComponent;
  let fixture: ComponentFixture<HeistAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeistAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeistAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
