import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { ProjectDetailComponent } from "./components/project-detail/project-detail.component";
import {EmployeeAdministrationComponent} from "./components/employee-administration/employee-administration.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'projectdetail', component: ProjectDetailComponent },
  { path: 'employeeadministration', component: EmployeeAdministrationComponent },
  { path: '**', redirectTo: 'login' }
];
