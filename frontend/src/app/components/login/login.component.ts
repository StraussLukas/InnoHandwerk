import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { Login } from '../../model/login';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(private client: HttpClient, private router: Router) {}

  confirmLogin(event: Event) {
    event.preventDefault();

    const emailInput = (document.getElementById('uname') as HTMLInputElement).value;
    const passwordInput = (document.getElementById('psw') as HTMLInputElement).value;

    this.client.get<Login>('http://localhost:8080/login/' + emailInput)
      .pipe(
        catchError(err => {
          if (err.status === 404) {
            alert('E-Mail oder Passwort ist falsch.');
          } else {
            alert('Ein Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.');
          }
          return of(null);  // Return an observable of null to complete the observable chain
        })
      )
      .subscribe(data => {
        if (data) {
          if (data.passwort === passwordInput) {
            this.router.navigate(['/dashboard', data.personalnummer]);
          } else {
            alert('E-Mail oder Passwort ist falsch.');
          }
        }
      });
  }
}
