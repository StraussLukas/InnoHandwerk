import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeAdministrationComponent } from './employee-administration.component';

describe('EmployeeAdministrationComponent', () => {
  let component: EmployeeAdministrationComponent;
  let fixture: ComponentFixture<EmployeeAdministrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeAdministrationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EmployeeAdministrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
