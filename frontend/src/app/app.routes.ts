import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ProjectDetailComponent } from './components/project-detail/project-detail.component';
import { EmployeeAdministrationComponent } from './components/employee-administration/employee-administration.component';
import { ProjectNewComponent } from './components/project-new/project-new.component';
import { ProjectEditComponent } from './components/project-edit/project-edit.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard/:personalnummer', component: DashboardComponent },
  { path: 'projectdetail/:personalnummer/:projectid', component: ProjectDetailComponent },
  { path: 'employeeadministration/:personalnummer', component: EmployeeAdministrationComponent },
  { path: 'projectnew/:personalnummer/:projectid', component: ProjectNewComponent },
  { path: 'projectedit/:personalnummer/:projectid', component: ProjectEditComponent },
  { path: '**', redirectTo: 'login' }
];
