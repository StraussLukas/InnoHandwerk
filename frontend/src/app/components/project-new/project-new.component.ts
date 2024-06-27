import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ProjectDisplayComponent } from '../shared/project-display/project-display.component';
import { ConstructionSite } from '../../model/constructionSite';

@Component({
  selector: 'app-project-new',
  standalone: true,
  imports: [CommonModule, ProjectDisplayComponent],
  templateUrl: './project-new.component.html',
  styleUrls: ['./project-new.component.css']
})
export class ProjectNewComponent {
  constructor(private http: HttpClient) {}

  createProject(formValue: ConstructionSite) {
    this.sendProjectToBackend(formValue);
  }

  sendProjectToBackend(project: Partial<ConstructionSite>) {
    const apiUrl = 'http://localhost:8080/baustelle';
    this.http.post(apiUrl, project).subscribe(
      response => {},
      error => {
        console.error('Error sending project to backend', error);
      }
    );
  }
}
