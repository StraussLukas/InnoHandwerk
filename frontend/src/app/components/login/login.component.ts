import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
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
      .subscribe(data => {
        if (data.passwort === passwordInput) {
          this.router.navigate(['/dashboard', data.personalnummer]);
        } else {
          alert('Email oder Passwort ist falsch.');
        }
      });
  }
}
