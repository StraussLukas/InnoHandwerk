import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Employee} from "../../model/employee";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private baseApiUrl = 'http://localhost:8080';  // Basis-URL des Backends

  constructor(private http: HttpClient) {}

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseApiUrl}/mitarbeiter`);
  }
}
