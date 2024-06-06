import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EmployeeListComponent } from '../shared/employee-list/employee-list.component';

interface Employee {
  image: string;
  name: string;
  birthday: Date;
  street: string;
  zip: string;
  city: string;
  accountNumber: string;
  imageRight: string;
}

@Component({
  selector: 'app-employee-administration',
  templateUrl: './employee-administration.component.html',
  styleUrls: ['./employee-administration.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, EmployeeListComponent]
})
export class EmployeeAdministrationComponent implements OnInit{
  showModal = false;
  employeeForm: FormGroup;
  formSubmitted = false;
  imagePreview: string | ArrayBuffer | null = null;
  selectedFile: File | null = null;
  employees: Employee[] = [];

  constructor(private fb: FormBuilder) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      birthday: ['', Validators.required],
      street: ['', Validators.required],
      zip: ['', Validators.required],
      city: ['', Validators.required],
      accountNumber: ['', Validators.required]
    });
  }


  ngOnInit() {
    this.addDefaultEmployee();
  }

  addDefaultEmployee() {
    const defaultEmployee: Employee = {
      image: '',
      name: 'John Doe',
      birthday: new Date('1990-01-01'),
      street: 'Musterstraße 1',
      zip: '12345',
      city: 'Musterstadt',
      accountNumber: 'DE1234567890',
      imageRight: '../../assets/trash-can.png'
    };
    this.employees.push(defaultEmployee);
  }

  openModal() {
    this.showModal = true;
    this.formSubmitted = false;
  }

  closeModal() {
    this.showModal = false;
    this.employeeForm.reset();
    this.imagePreview = null;
    this.selectedFile = null;
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

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
      };
      reader.readAsDataURL(file);
    }
  }

  addEmployee() {
    console.log ("Aufruf erfolgreich)")
    this.formSubmitted = true;
    if (this.employeeForm.valid) {
      const newEmployee = { ...this.employeeForm.value, image: this.imagePreview as string, imageRight: '../../assets/trash-can.png' };
      this.employees.push(newEmployee);
      this.closeModal();
    } else {
      console.log("Formular ungültig");
      alert('Bitte alle Felder korrekt ausfüllen');
    }
  }

  onEmployeeDeleted(index: number) {
    this.employees.splice(index, 1);
  }
}
