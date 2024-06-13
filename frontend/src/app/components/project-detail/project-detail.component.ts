import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { ProjectInfoTileComponent } from '../shared/project-info-tile/project-info-tile.component';
import { ChatMessageComponent } from '../shared/chat-message/chat-message.component';
import { CommonModule } from '@angular/common';
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {ConstructionSite} from "../../model/constructionSite";

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [ProjectInfoTileComponent, ChatMessageComponent, CommonModule, FormsModule],
  templateUrl: './project-detail.component.html',
  styleUrl: './project-detail.component.css'
})
export class ProjectDetailComponent implements OnInit{
  project: ConstructionSite = {};

  messages = [
    { text: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.  Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer', timestamp: new Date(), user: 'Max Müller', images: ['assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg','assets/testdaten/fertighausexperte.jpg'] },
    { text: 'Zweiter Kommentar', timestamp: new Date(), user: 'Anna Schmidt', images: [] }
  ];

  newMessage: string = '';
  selectedFiles: File[] = [];

  projectIDUrl?: number | null;

  @ViewChild('fileInput') fileInput!: ElementRef;

  constructor(private client: HttpClient, private route: ActivatedRoute) {}

  ngOnInit() {
    const parameterFromUrl = this.route.snapshot.paramMap.get('projectid');
    if (parameterFromUrl !== null && !isNaN(+parameterFromUrl)) {
      this.projectIDUrl = +parameterFromUrl;
    } else {
      this.projectIDUrl = null;
    }
    this.client.get<ConstructionSite>(`http://localhost:8080/baustelle/${this.projectIDUrl}`)
      .subscribe(data => {
        this.project = data;
      }, error => {
        console.error('Fehler beim Laden der Baustelle:', error);
      });
  }

  sendMessage() {
    if (this.newMessage.trim()) {
      const newMsg = {
        text: this.newMessage,
        timestamp: new Date(),
        user: 'Aktueller Benutzer',
        images: this.selectedFiles.map(file => URL.createObjectURL(file))
      };

      this.messages.push(newMsg);
      this.newMessage = '';
      this.selectedFiles = [];
      this.fileInput.nativeElement.value = '';
    }
  }

  onFileSelected(event: any) {
    const files: File[] = Array.from(event.target.files);
    this.selectedFiles.push(...files);
  }
}
