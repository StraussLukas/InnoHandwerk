import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { ProjectDetailComponent } from "./components/project-detail/project-detail.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'projectdetail', component: ProjectDetailComponent },
  { path: '**', redirectTo: 'login' }
];
