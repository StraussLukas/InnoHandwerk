import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProjectNewComponent } from './project-new.component';

describe('ProjectNewComponent', () => {
  let component: ProjectNewComponent;
  let fixture: ComponentFixture<ProjectNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectNewComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ProjectNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
