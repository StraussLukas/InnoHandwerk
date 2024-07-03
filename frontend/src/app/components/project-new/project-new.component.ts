import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ProjectDisplayComponent } from '../shared/project-display/project-display.component';
import { ConstructionSite } from '../../model/constructionSite';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-project-new',
  standalone: true,
  imports: [CommonModule, ProjectDisplayComponent],
  templateUrl: './project-new.component.html',
  styleUrls: ['./project-new.component.css']
})
export class ProjectNewComponent {
  private personalnummer: number | null = null;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    this.route.paramMap.subscribe(params => {
      this.personalnummer = +params.get('personalnummer')!;
    });
  }

  createProject(formValue: ConstructionSite) {
    this.sendProjectToBackend(formValue);
  }

  sendProjectToBackend(project: Partial<ConstructionSite>) {
    const apiUrl = 'http://localhost:8080/baustelle';
    this.http.post(apiUrl, project).subscribe(
      response => {
        // Navigate to the dashboard page
        if (this.personalnummer !== null) {
          this.router.navigate([`/dashboard/${this.personalnummer}`]);
        }
      },
      error => {
        console.error('Error sending project to backend', error);
      }
    );
  }
}
