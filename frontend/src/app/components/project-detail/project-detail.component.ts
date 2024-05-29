import { Component } from '@angular/core';
import { ProjectInfoTileComponent } from '../shared/project-info-tile/project-info-tile.component';
import { ChatMessageComponent } from '../shared/chat-message/chat-message.component';
import { CommonModule } from '@angular/common';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [ProjectInfoTileComponent, ChatMessageComponent, CommonModule, FormsModule],
  templateUrl: './project-detail.component.html',
  styleUrl: './project-detail.component.css'
})
export class ProjectDetailComponent {
  project = {
    image: 'assets/testdaten/baustelle-rohbau-einfamilienhaus-superingo-adobestock.jpg',
    title: 'Rohbau Einfamilienhaus',
    name: 'Bauprojekt Meier',
    address: 'Musterstraße 1, 12345 Musterstadt',
    status: 'In Bearbeitung',
    employees: ['Max Müller', 'Anna Schmidt', 'Jan Becker']
  };

  messages = [
    { text: 'Erster Kommentar', timestamp: new Date(), user: 'Max Müller' },
    { text: 'Zweiter Kommentar', timestamp: new Date(), user: 'Anna Schmidt' }
  ];

  newMessage: string = '';

  sendMessage() {
    if (this.newMessage.trim()) {
      this.messages.push({
        text: this.newMessage,
        timestamp: new Date(),
        user: 'Aktueller Benutzer'
      });
      this.newMessage = '';
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.messages.push({
        text: `Datei hochgeladen: ${file.name}`,
        timestamp: new Date(),
        user: 'Aktueller Benutzer'
      });
    }
  }
}
