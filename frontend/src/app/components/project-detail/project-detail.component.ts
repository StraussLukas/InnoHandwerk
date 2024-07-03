import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProjectInfoTileComponent } from '../shared/project-info-tile/project-info-tile.component';
import { ChatMessageComponent } from '../shared/chat-message/chat-message.component';
import { CommonModule } from '@angular/common';
import { Employee } from '../../model/employee';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { ConstructionSite } from '../../model/constructionSite';

export interface Beitrag {
  id?: number;
  freitext?: string;
  baustelleId?: number;
  personalnummer?: number;
  zeitstempel?: string;
}

export interface Baustellenbesetzung {
  id?: number;
  personalnummer?: number;
  baustellenId?: number;
  datum?: string;
  uhrzeitVon?: string;
  uhrzeitBis?: string;
}

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [ProjectInfoTileComponent, ChatMessageComponent, CommonModule, FormsModule],
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {
  project: ConstructionSite = {};
  messages: { personalnummer: number, timestamp?: Date, user?: string, text: string, images: string[] }[] = [];
  newMessage: string = '';
  selectedFiles: File[] = [];
  projectIDUrl?: number | null;
  personalnummerUrl!: number | null;
  mitarbeiterDaten: Employee = {};
  showModal: boolean = false;
  currentDate: string = new Date().toISOString().split('T')[0];
  employees: Employee[] = [];
  selectedEmployee: Employee | null = null;
  newEmployeeDate: string = this.currentDate;
  newEmployeeStartTime: string = '08:00';
  newEmployeeEndTime: string = '16:00';
  assignedEmployees: Employee[] = [];

  @ViewChild('fileInput') fileInput!: ElementRef;

  constructor(private client: HttpClient, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    const projectIdParam = this.route.snapshot.paramMap.get('projectid');
    if (projectIdParam !== null && !isNaN(+projectIdParam)) {
      this.projectIDUrl = +projectIdParam;
    } else {
      this.projectIDUrl = null;
    }

    const personalnummerParam = this.route.snapshot.paramMap.get('personalnummer');
    if (personalnummerParam !== null && !isNaN(+personalnummerParam)) {
      this.personalnummerUrl = +personalnummerParam;
    } else {
      this.personalnummerUrl = null;
    }

    if (this.projectIDUrl !== null) {
      const projectUrl = `http://localhost:8080/baustelle/${this.projectIDUrl}`;
      this.client.get<ConstructionSite>(projectUrl).subscribe(
        data => {
          this.project = data;
        },
        error => {
          console.error('Fehler beim Laden der Baustelle:', error);
        }
      );

      if (this.personalnummerUrl !== null) {
        this.client.get<Employee>(`http://localhost:8080/mitarbeiter/${this.personalnummerUrl}`).subscribe(
          data => {
            this.mitarbeiterDaten.vorname = data.vorname;
            this.mitarbeiterDaten.nachname = data.nachname;
            this.mitarbeiterDaten.admin = data.admin;
            this.mitarbeiterDaten.personalnummer = data.personalnummer;
          },
          error => {
            console.error('Fehler beim Laden der Mitarbeiterdaten:', error);
          }
        );
      }

      this.loadMessages(this.projectIDUrl);
      this.loadEmployeesForDate(this.currentDate);
    }
    this.loadAllEmployees();
  }

  loadEmployeeData(personalnummer: number): Observable<Employee> {
    return this.client.get<Employee>(`http://localhost:8080/mitarbeiter/${personalnummer}`).pipe(
      catchError(error => {
        console.error('Fehler beim Laden der Mitarbeiterdaten:', error);
        return of({ vorname: 'Unbekannt', nachname: '' } as Employee);
      })
    );
  }

  loadMessages(baustellenId: number) {
    let testMessages: { personalnummer: number, timestamp?: Date, user?: string, text: string, images: string[] }[] = [];

    if (baustellenId === 1) {
      testMessages = [
        {
          personalnummer: 100,
          text: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
          timestamp: new Date('2024-06-23T09:30:00'),
          user: "Hans Müller",
          images: ["assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg", "assets/testdaten/fertighausexperte.jpg"]
        },
        {
          personalnummer: 100,
          text: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
          timestamp: new Date('2024-06-24T19:30:00'),
          user: "Hans Müller",
          images: []
        }
      ];
    }

    const messagesUrl = `http://localhost:8080/beitraege`;
    this.client.get<Beitrag[]>(messagesUrl).subscribe(
      data => {
        const employeeObservables = data.filter(beitrag => beitrag.baustelleId === baustellenId).map(beitrag =>
          this.loadEmployeeData(beitrag.personalnummer!).pipe(
            map(employee => ({
              personalnummer: beitrag.personalnummer!,
              text: beitrag.freitext || '',
              timestamp: new Date(beitrag.zeitstempel || ''),
              user: `${employee.vorname} ${employee.nachname}`,
              images: []
            }))
          )
        );

        forkJoin(employeeObservables).subscribe(messages => {
          this.messages = [...testMessages, ...messages];
        });
      },
      error => {
        console.error('Fehler beim Laden der Nachrichten:', error);
      }
    );
  }

  sendMessage() {
    if (this.newMessage.trim() && this.projectIDUrl !== null) {
      const newMsg: Beitrag = {
        freitext: this.newMessage,
        baustelleId: this.projectIDUrl,
        personalnummer: this.mitarbeiterDaten.personalnummer!,
      };

      this.client.post<Beitrag>('http://localhost:8080/beitrag', newMsg).subscribe(
        response => {
          this.newMessage = '';
          this.selectedFiles = [];
          this.fileInput.nativeElement.value = '';
        },
        error => {
          console.error('Fehler beim Senden der Nachricht:', error);
        }
      );
      window.location.reload();
    }
  }

  onFileSelected(event: any) {
    const files: File[] = Array.from(event.target.files);
    this.selectedFiles.push(...files);
  }

  openModal() {
    this.showModal = true;
    this.selectedEmployee = null;
    this.newEmployeeDate = this.currentDate;
    this.newEmployeeStartTime = '08:00';
    this.newEmployeeEndTime = '16:00';
  }

  closeModal() {
    this.showModal = false;
  }

  loadAllEmployees() {
    this.client.get<Employee[]>('http://localhost:8080/mitarbeiter').subscribe(
      data => {
        this.employees = data;
      },
      error => {
        console.error('Fehler beim Laden der Mitarbeiterliste:', error);
      }
    );
  }

  addEmployeeToDate() {
    if (this.selectedEmployee && this.projectIDUrl !== null) {
      const newAssignment: Baustellenbesetzung = {
        personalnummer: this.selectedEmployee.personalnummer,
        baustellenId: this.projectIDUrl,
        datum: this.newEmployeeDate,
        uhrzeitVon: `${this.newEmployeeStartTime}:00`,
        uhrzeitBis: `${this.newEmployeeEndTime}:00`
      };

      this.client.post<Baustellenbesetzung>('http://localhost:8080/baustellenBesetzung', newAssignment).subscribe(
        response => {
          this.closeModal();
          this.loadEmployeesForDate(this.newEmployeeDate);
        },
        error => {
          console.error('Fehler beim Hinzufügen des Mitarbeiters:', error);
        }
      );
    }
  }

  loadEmployeesForDate(date: string) {
    if (this.projectIDUrl !== null) {
      this.assignedEmployees = [];
      this.client.get<Baustellenbesetzung[]>(`http://localhost:8080/baustellenBesetzung/${this.projectIDUrl}?datum=${date}`).subscribe(
        data => {
          const employeeObservables = data.map(besetzung =>
            this.loadEmployeeData(besetzung.personalnummer!).pipe(
              map(employee => ({
                personalnummer: besetzung.personalnummer!,
                vorname: employee.vorname,
                nachname: employee.nachname
              }))
            )
          );

          forkJoin(employeeObservables).subscribe(employees => {
            this.assignedEmployees = employees;
          });
        },
        error => {
          console.error('Fehler beim Laden der Mitarbeiter:', error);
        }
      );
    }
  }

  onDateChange(event: Event) {
    const input = event.target as HTMLInputElement;
    const newDate = input.value;
    this.currentDate = newDate;
    this.loadEmployeesForDate(newDate);
  }

  getEmployeeNames(): string {
    return this.assignedEmployees.map(e => `${e.vorname} ${e.nachname}`).join(', ');
  }

  navigateToEdit() {
    if (this.projectIDUrl !== null && this.personalnummerUrl !== null) {
      this.router.navigate([`/projectedit/${this.personalnummerUrl}/${this.projectIDUrl}`]);
    } else {
      console.error('Projekt- oder Personalnummer fehlen.');
    }
  }
}
