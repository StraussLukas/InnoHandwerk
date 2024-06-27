import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-minimal-header',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './minimal-header.component.html',
  styleUrl: './minimal-header.component.css'
})
export class MinimalHeaderComponent implements OnInit{
  personalnummerUrl!: string;

  constructor(
    private router: Router
  ) {
    this.router.events.subscribe(() => {
      const currentUrl = this.router.url;
      const urlParts = currentUrl.split('/');
      this.personalnummerUrl = urlParts[2];
    });
  }

  ngOnInit() {

  }

}
