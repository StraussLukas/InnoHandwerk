import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MinimalHeaderComponent } from "./components/minimal-header/minimal-header.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MinimalHeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
