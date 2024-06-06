import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinimalHeaderComponent } from './minimal-header.component';

describe('MinimalHeaderComponent', () => {
  let component: MinimalHeaderComponent;
  let fixture: ComponentFixture<MinimalHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MinimalHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MinimalHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
