import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeistListComponent } from './heist-list.component';

describe('HeistListComponent', () => {
  let component: HeistListComponent;
  let fixture: ComponentFixture<HeistListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeistListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeistListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
