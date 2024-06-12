import { Component } from '@angular/core';
import {ProjectInfoTileComponent} from "../shared/project-info-tile/project-info-tile.component";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";

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
export class DashboardComponent {
  projects = [
    {
      image: 'assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg',
      title: 'Rohbau Einfamilienhaus',
      name: 'Bauprojekt Meier',
      address: 'Musterstraße 1, 12345 Musterstadt',
      status: 'In Bearbeitung',
      employees: ['Max Müller', 'Anna Schmidt', 'Jan Becker']
    },
    {
      image: 'assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg',
      title: 'Brückenbauprojekt',
      name: 'Projekt Brücke',
      address: 'Hauptstraße 15, 23456 Beispielstadt',
      status: 'Abgeschlossen',
      employees: ['Lisa König', 'Markus Weber', 'Clara Fischer']
    },
    {
      image: 'asdf',
      title: 'Bürogebäude Neubau',
      name: 'Bürozentrum GmbH',
      address: 'Neubauweg 3, 34567 Neustadt',
      status: 'In Planung',
      employees: ['Paul Huber', 'Monika Schulz', 'Peter Hoffmann']
    },
    {
      image: 'assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg',
      title: 'Torre Eiffel',
      name: 'Projekt Paris',
      address: 'Eiffelturm, Paris, Frankreich',
      status: 'In Planung',
      employees: ['Jean Dupont', 'Marie Curie', 'Pierre Renard']
    },
    {
      image: 'assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg',
      title: 'Fitnessstudio',
      name: 'Sportzentrum AG',
      address: 'Fitnesstraße 8, 45678 Sportstadt',
      status: 'Fertiggestellt',
      employees: ['Lukas Mayer', 'Tina Becker', 'Julia Wagner']
    }
  ];

  filterType: string = '';
  filterValue: string = '';

  filterTypes = [
    { value: '', label: 'Kein Filter' },
    { value: 'employee', label: 'Mitarbeiter' },
    { value: 'status', label: 'Status' }
  ];

  employeeNames = ['Tobias Keller', 'Florian Wagner', 'Sebastian Meier', 'Daniel Fischer', 'Andreas Schulze', 'Katrin Hoffmann', 'Laura Becker', 'Julia Neumann', 'Lena Schneider', 'Martina Krüger', 'Thomas Müller', 'Sarah Braun', 'Michael König', 'Anja Richter', 'Lukas Bauer', 'Christina Wolf', 'Patrick Schäfer', 'Sandra Hartmann', 'Kevin Lehmann', 'Claudia Weber']
  statuses = ['In Planung', 'Fertiggestellt', 'Abgeschlossen'];

  onFilterTypeChange() {
    this.filterValue = '';
  }

  applyFilter() {
    //Call ans Backend
  }

}