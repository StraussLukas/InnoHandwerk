import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

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
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class EmployeeListComponent {
  @Input() employees: Employee[] = [];
  @Output() employeeDeleted = new EventEmitter<number>();

  deleteEmployee(index: number) {
    this.employeeDeleted.emit(index);
  }
}
