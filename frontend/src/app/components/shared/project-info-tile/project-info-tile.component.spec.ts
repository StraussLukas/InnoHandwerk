import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectInfoTileComponent } from './project-info-tile.component';

describe('ProjectInfoTileComponent', () => {
  let component: ProjectInfoTileComponent;
  let fixture: ComponentFixture<ProjectInfoTileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectInfoTileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProjectInfoTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
