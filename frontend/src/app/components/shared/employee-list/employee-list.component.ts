import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Employee {
  image: string;
  vorname: string;
  nachname: string;
  email: string;
  personalnummer: number;
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
    const employeeId = this.employees[index].personalnummer;
    this.employeeDeleted.emit(employeeId);
  }
}
