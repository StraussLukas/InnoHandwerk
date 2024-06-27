import { Component, OnInit} from '@angular/core';
import {ProjectInfoTileComponent} from "../shared/project-info-tile/project-info-tile.component";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute } from "@angular/router";
import {Employee} from "../../model/employee";
import {ConstructionSite} from "../../model/constructionSite";
import {ConstructionSiteService} from "../../services/construction/construction-site.service";
import {EmployeeService} from "../../services/employee/employee.service";
import { Router } from '@angular/router';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    ProjectInfoTileComponent,
    NgForOf,
    FormsModule,
    NgIf
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{
  projects: ConstructionSite[] = [];

  filterType: string = '';
  filterValue: string = '';

  filterTypes = [
    { value: '', label: 'Kein Filter' },
    { value: 'employee', label: 'Mitarbeiter' },
    { value: 'status', label: 'Status' }
  ];

  employees: Employee[] = [];
  statuses = ['Erstellt', 'In Arbeit', 'Abgeschlossen'];
  personalnummerUrl!: number | null;
  mitarbeiterDaten: Employee = {};

  isApplyFilter: boolean = false;

  constructor(
    private client: HttpClient,
    private route: ActivatedRoute,
    private constructionSiteService: ConstructionSiteService,
    private employeeService: EmployeeService,
    private router: Router
  ) {}

  ngOnInit() {
    const parameterFromUrl = this.route.snapshot.paramMap.get('personalnummer');
    if (parameterFromUrl !== null && !isNaN(+parameterFromUrl)) {
      this.personalnummerUrl = +parameterFromUrl;
    } else {
      this.personalnummerUrl = null;
    }
    this.client.get<Employee>(`http://localhost:8080/mitarbeiter/${this.personalnummerUrl}`)
      .subscribe(data => {
        this.mitarbeiterDaten.vorname = data.vorname;
        this.mitarbeiterDaten.nachname = data.nachname;
        this.mitarbeiterDaten.admin = data.admin;
      }, error => {
        console.error('Fehler beim Laden der Mitarbeiterdaten:', error);
      });
    this.loadEmployeeData();
    this.loadProjects();
  }

  loadProjects() {
    this.projects = [];
    this.constructionSiteService.getAllConstructionSites().subscribe(
      (data: ConstructionSite[]) => {
        this.projects = data;
      },
      error => {
        console.error('Fehler beim Laden der Projekte:', error);
      }
    );
  }

  loadEmployeeData() {
    this.employeeService.getAllEmployees().subscribe(
      (data: Employee[]) => {
        // Wir speichern die empfangenen Employee-Objekte in das Array employees.
        this.employees = data;
      },
      error => {
        console.error('Fehler beim Laden der Mitarbeiterdaten:', error);
      }
    );
  }

  onFilterTypeChange() {
    this.filterValue = '';
  }

  applyFilter() {
    this.projects = [];

    if (this.filterType === 'employee' && this.filterValue) {
      const selectedEmployee: Employee = this.filterValue as Employee;

      if (selectedEmployee.personalnummer !== undefined && selectedEmployee.personalnummer !== null) {
        this.constructionSiteService.getConstructionSitesByPersonalnummer(selectedEmployee.personalnummer.toString())
          .subscribe(
            (data: ConstructionSite[]) => {
              this.projects = data;
            },
            error => {
              console.error('Fehler beim Filtern der Projekte:', error);
            }
          );
      } else {
        console.error('Die Personalnummer des ausgewählten Mitarbeiters ist nicht definiert.');
      }

    } else if (this.filterType === 'status' && this.filterValue) {
      this.constructionSiteService.getConstructionSitesByStatus(this.filterValue)
        .subscribe(
          (data: ConstructionSite[]) => {
            this.projects = data;
          },
          error => {
            console.error('Fehler beim Filtern der Projekte:', error);
          }
        );
    } else {
      this.loadProjects();
    }
  }

  navigateToEmployeeManagement() {
    this.router.navigate(['/employeeadministration']);
  }

  navigateToProject() {
    this.router.navigate(['/projectnew']);
  }



}
