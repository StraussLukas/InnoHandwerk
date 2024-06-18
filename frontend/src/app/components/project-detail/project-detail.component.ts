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
  messages: { text: string, timestamp: Date, user: string, images: string[] }[] = [];
  newMessage: string = '';
  selectedFiles: File[] = [];
  projectIDUrl?: number | null;
  personalnummerUrl!: number | null;

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
    const messagesUrl = `http://localhost:8080/beitrag`;
    console.log('Loading messages from URL:', messagesUrl);
    this.client.get<Beitrag[]>(messagesUrl)
      .subscribe(data => {
        const employeeObservables = data.filter(beitrag => beitrag.baustelleId === baustellenId)
          .map(beitrag =>
            this.loadEmployeeData(beitrag.personalnummer!).pipe(
              map(employee => ({
                text: beitrag.freitext || '',
                timestamp: new Date(beitrag.zeitstempel || ''),
                user: `${employee.vorname} ${employee.nachname}`,
                images: [] // Assuming no images provided in `Beitrag`
              }))
            )
          );

        forkJoin(employeeObservables).subscribe(messages => {
          this.messages = messages;
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
        personalnummer: 100, // Beispielbenutzer
        zeitstempel: new Date().toISOString(),
      };

      // Nachricht an Backend senden
      this.client.post<Beitrag>('http://localhost:8080/beitrag', newMsg)
        .subscribe(
          response => {
            this.loadEmployeeData(response.personalnummer!).subscribe(employee => {
              this.messages.push({
                text: response.freitext || '',
                timestamp: new Date(response.zeitstempel || ''),
                user: `${employee.vorname} ${employee.nachname}`,
                images: this.selectedFiles.map(file => URL.createObjectURL(file))
              });
            });
            this.newMessage = '';
            this.selectedFiles = [];
            this.fileInput.nativeElement.value = '';
          },
          error => {
            console.error('Fehler beim Senden der Nachricht:', error);
          }
        );
    }
  }

  onFileSelected(event: any) {
    const files: File[] = Array.from(event.target.files);
    this.selectedFiles.push(...files);
  }
}
