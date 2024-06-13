import { Component, OnInit} from '@angular/core';
import {ProjectInfoTileComponent} from "../shared/project-info-tile/project-info-tile.component";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute } from "@angular/router";
import {Employee} from "../../model/employee";
import {ConstructionSite} from "../../model/constructionSite";
import {ConstructionSiteService} from "../../services/construction-site.service";
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

  employeeNames = ['Tobias Keller', 'Florian Wagner', 'Sebastian Meier', 'Daniel Fischer', 'Andreas Schulze', 'Katrin Hoffmann', 'Laura Becker', 'Julia Neumann', 'Lena Schneider', 'Martina Krüger', 'Thomas Müller', 'Sarah Braun', 'Michael König', 'Anja Richter', 'Lukas Bauer', 'Christina Wolf', 'Patrick Schäfer', 'Sandra Hartmann', 'Kevin Lehmann', 'Claudia Weber']
  statuses = ['In Planung', 'Fertiggestellt', 'Abgeschlossen'];
  personalnummerUrl!: number | null;
  mitarbeiterDaten: Employee = {};

  constructor(private client: HttpClient, private route: ActivatedRoute, private constructionSiteService: ConstructionSiteService) {}

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

    this.loadProjects();
  }

  loadProjects() {
    this.constructionSiteService.getAllConstructionSites().subscribe(
      (data: ConstructionSite[]) => {
        this.projects = data;
      },
      error => {
        console.error('Fehler beim Laden der Projekte:', error);
      }
    );
  }

  onFilterTypeChange() {
    this.filterValue = '';
  }

  applyFilter() {
    //Call ans Backend
  }

}
