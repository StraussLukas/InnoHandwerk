import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProjectInfoTileComponent } from '../shared/project-info-tile/project-info-tile.component';
import { ChatMessageComponent } from '../shared/chat-message/chat-message.component';
import { CommonModule } from '@angular/common';
import { Employee } from '../../model/employee';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { ConstructionSite } from '../../model/constructionSite';

export interface Beitrag {
  id?: number;
  freitext?: string;
  baustelleId?: number;
  personalnummer?: number;
  zeitstempel?: string;
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

  @ViewChild('fileInput') fileInput!: ElementRef;

  constructor(private client: HttpClient, private route: ActivatedRoute) {}

  ngOnInit() {
    const parameterFromUrl = this.route.snapshot.paramMap.get('projectid');
    if (parameterFromUrl !== null && !isNaN(+parameterFromUrl)) {
      this.projectIDUrl = +parameterFromUrl;
    } else {
      this.projectIDUrl = null;
    }

    if (this.projectIDUrl !== null) {
      const projectUrl = `http://localhost:8080/baustelle/${this.projectIDUrl}`;
      console.log('Loading project from URL:', projectUrl);
      this.client.get<ConstructionSite>(projectUrl)
        .subscribe(data => {
          this.project = data;
        }, error => {
          console.error('Fehler beim Laden der Baustelle:', error);
        });

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
          this.mitarbeiterDaten.personalnummer= data.personalnummer;
        }, error => {
          console.error('Fehler beim Laden der Mitarbeiterdaten:', error);
        });

      this.loadMessages(this.projectIDUrl);
    }
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
    let testMessages: { personalnummer: number, timestamp?: Date, user?: string, text: string, images: string[] }[]  = [];

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
    console.log('Loading messages from URL:', messagesUrl);
    this.client.get<Beitrag[]>(messagesUrl)
      .subscribe(data => {
        const employeeObservables = data.filter(beitrag => beitrag.baustelleId === baustellenId)
          .map(beitrag =>
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

      }, error => {
        console.error('Fehler beim Laden der Nachrichten:', error);
      });
  }

  sendMessage() {
    if (this.newMessage.trim() && this.projectIDUrl !== null) {
      const newMsg: Beitrag = {
        freitext: this.newMessage,
        baustelleId: this.projectIDUrl,
        personalnummer: this.mitarbeiterDaten.personalnummer!,
      };

      // Nachricht an Backend senden
      this.client.post<Beitrag>('http://localhost:8080/beitrag', newMsg)
        .subscribe(
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
}
