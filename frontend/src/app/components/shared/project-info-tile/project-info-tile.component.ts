import {Component, Input} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {ConstructionSite} from "../../../model/constructionSite";
import {Router} from "@angular/router";

@Component({
  selector: 'app-project-info-tile',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './project-info-tile.component.html',
  styleUrl: './project-info-tile.component.css'
})
export class ProjectInfoTileComponent {

  @Input() project: ConstructionSite | undefined;
  @Input() isDashboard = false;
  @Input() personalnummer: number | null = null;

  constructor(private router: Router) {}

  navigateToProjectDetail() {
    if (this.project && this.project.id && this.personalnummer) {
      this.router.navigate([`/projectdetail/${this.personalnummer}/${this.project.id}`]);
    } else {
      console.error('Projekt- oder Personalnummer fehlen.');
    }
  }
}
