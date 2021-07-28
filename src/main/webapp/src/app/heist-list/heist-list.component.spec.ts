import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeistListComponent } from './heist-list.component';
import {HeistService} from '../services/heist.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

import {HeistInfo} from '../models/heist-info';

describe('HeistListComponent', () => {
  let component: HeistListComponent;
  let fixture: ComponentFixture<HeistListComponent>;
  let heistService: HeistService;

  const mockedData: HeistInfo[] = [
    {
      id: 100,
      name: 'European Central Bank - ECB',
      status: 'FINISHED'
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeistListComponent ],
      imports: [HttpClientTestingModule]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeistListComponent);
    component = fixture.componentInstance;
    heistService = fixture.debugElement.injector.get(HeistService);
    fixture.detectChanges();
  });

  it('should list all heists', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('nb-list').textContent).toContain(component.heists.toString());
  });



});
