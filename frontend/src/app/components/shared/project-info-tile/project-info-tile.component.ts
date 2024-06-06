import {Component, input, Input} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";

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

  @Input() project: any;
  @Input() isDashboard = false;
}
