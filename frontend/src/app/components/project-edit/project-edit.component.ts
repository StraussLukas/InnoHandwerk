import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ProjectDisplayComponent } from '../shared/project-display/project-display.component';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ConstructionSite } from '../../model/constructionSite';

@Component({
  selector: 'app-project-edit',
  standalone: true,
  imports: [CommonModule, ProjectDisplayComponent, ReactiveFormsModule],
  templateUrl: './project-edit.component.html',
  styleUrls: ['./project-edit.component.css']
})
export class ProjectEditComponent implements OnInit {
  projectForm: FormGroup;
  private projectId = 10;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.projectForm = this.fb.group({
      id: [this.projectId],
      titel: ['', Validators.required],
      name_bauherr: ['', Validators.required],
      adresse: ['', Validators.required],
      status: ['', Validators.required],
      telefon: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      arbeitsaufwand: ['', Validators.required],
      zeitstempel: ['']
    });
  }

  ngOnInit(): void {
    this.loadProject();
  }

  loadProject() {
    const apiUrl = `http://localhost:8080/baustelle/${this.projectId}`;
    this.http.get<ConstructionSite>(apiUrl).subscribe(
      data => {
        this.projectForm.patchValue(data);
      },
      error => {
        console.error('Fehler beim Laden des Projekts:', error);
      }
    );
  }

  saveProject(formValue: any) {
    this.sendProjectToBackend(formValue);
  }

  sendProjectToBackend(project: Partial<ConstructionSite>) {
    const apiUrl = `http://localhost:8080/baustelle/${this.projectId}`;
    this.http.put(apiUrl, project).subscribe(
      response => {},
      error => {
        console.error('Fehler beim Aktualisieren des Projekts:', error);
      }
    );
  }
}
