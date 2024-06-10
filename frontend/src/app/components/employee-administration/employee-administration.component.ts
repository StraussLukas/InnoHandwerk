import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EmployeeListComponent } from '../shared/employee-list/employee-list.component';
import { HttpClient } from '@angular/common/http';

interface Employee {
  image: string;
  vorname: string;
  nachname: string;
  email: string;
  personalnummer: number;
  imageRight: string;
}

@Component({
  selector: 'app-employee-administration',
  templateUrl: './employee-administration.component.html',
  styleUrls: ['./employee-administration.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, EmployeeListComponent]
})
export class EmployeeAdministrationComponent implements OnInit {
  showModal = false;
  employeeForm: FormGroup;
  formSubmitted = false;
  imagePreview: string | ArrayBuffer | null = null;
  selectedFile: File | null = null;
  employees: Employee[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.employeeForm = this.fb.group({
      vorname: ['', Validators.required],
      nachname: ['', Validators.required],
      email: ['', Validators.required],
      personalnummer: ['', Validators.required],
    });
  }

  ngOnInit() {
    this.fetchEmployees();
  }

  async fetchEmployees() {
    const employeeApiUrl = 'http://localhost:8080/mitarbeiter';
    const imageRightPath = '../../assets/trash-can.png';

    try {
      const response = await fetch(employeeApiUrl);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data: Employee[] = await response.json();
      this.employees = data.map(employee => ({ ...employee, imageRight: imageRightPath }));
    } catch (error) {
      console.error('Error fetching employees:', error);
    }
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
        this.employeeForm.patchValue({ image: this.imagePreview as string });
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
        this.employeeForm.patchValue({ image: this.imagePreview as string });
      };
      reader.readAsDataURL(file);
    }
  }

  addEmployee() {
    console.log("Aufruf erfolgreich");
    this.formSubmitted = true;
    if (this.employeeForm.valid) {
      const newEmployee = {
        ...this.employeeForm.value,
        imageRight: '../../assets/trash-can.png'
      };
      this.employees.push(newEmployee);
      this.sendEmployeeToBackend(newEmployee);
      this.closeModal();
    } else {
      console.log("Formular ungültig");
      alert('Bitte alle Felder korrekt ausfüllen');
    }
  }

  onEmployeeDeleted(employeeId: number) {
    const index = this.employees.findIndex(employee => employee.personalnummer === employeeId);
    if (index > -1) {
      this.deleteEmployeeFromBackend(employeeId);
      this.employees.splice(index, 1);
    }
  }

  deleteEmployeeFromBackend(employeeId: number) {
    const employeeApiUrl = `http://localhost:8080/mitarbeiter/${employeeId}`;
    this.http.delete(employeeApiUrl).subscribe(
      (response: any) => {
        console.log('Employee successfully deleted from backend', response);
      },
      (error: any) => {
        console.error('Error deleting employee from backend', error);
      }
    );
  }

  sendEmployeeToBackend(employee: Partial<Employee>) {
    const employeeApiUrl = 'http://localhost:8080/mitarbeiter';
    const { image, ...employeeData } = employee;

    this.http.post(employeeApiUrl, employeeData).subscribe(
      (response: any) => {
        console.log('Employee successfully sent to backend', response);
      },
      (error: any) => {
        console.error('Error sending employee to backend', error);
      }
    );
  }
}
