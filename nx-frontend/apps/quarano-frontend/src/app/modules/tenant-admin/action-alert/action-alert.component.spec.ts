/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import {DebugElement, NO_ERRORS_SCHEMA} from '@angular/core';

import { ActionAlertComponent } from './action-alert.component';
import {alertConfigurations} from './AlertConfiguration';

describe('ActionAlertComponent', () => {
  let component: ActionAlertComponent;
  let fixture: ComponentFixture<ActionAlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActionAlertComponent ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionAlertComponent);
    component = fixture.componentInstance;
    component.alert = alertConfigurations()[0];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});