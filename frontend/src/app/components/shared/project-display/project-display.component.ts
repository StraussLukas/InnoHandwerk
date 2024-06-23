import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ConstructionSite } from "../../../model/constructionSite";

@Component({
  selector: 'app-project-display',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './project-display.component.html',
  styleUrls: ['./project-display.component.css']
})
export class ProjectDisplayComponent {
  @Input() buttonLabel: string = 'Erstellen';
  @Output() formSubmit: EventEmitter<any> = new EventEmitter();

  @Input() projectForm: FormGroup = this.fb.group({
    id: [10],
    titel: ['', Validators.required],
    name_bauherr: ['', Validators.required],
    adresse: ['', Validators.required],
    status: ['', Validators.required],
    telefon: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    arbeitsaufwand: ['', Validators.required],
    zeitstempel: [new Date().toISOString(), Validators.required]
  });
  imagePreview: string | ArrayBuffer | null = null;
  selectedFile: File | null = null;

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    const file = event.dataTransfer?.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
        this.projectForm.patchValue({ image: reader.result });
        console.log('Image preview updated:', this.imagePreview);
      };
      reader.readAsDataURL(file);
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
        this.projectForm.patchValue({ image: reader.result });
        console.log('Image preview updated:', this.imagePreview);
      };
      reader.readAsDataURL(file);
    }
  }

  deleteImage() {
    this.imagePreview = null;
    this.selectedFile = null;
    this.projectForm.patchValue({ image: null });
    console.log('Image deleted');
  }

  selectFile() {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    fileInput.click();
  }

  submitForm() {
    if (this.projectForm.valid) {
      console.log('Form is valid, submitting:', this.projectForm.value);
      this.formSubmit.emit(this.projectForm.value);
    } else {
      console.log('Form is invalid');
    }
  }
}
