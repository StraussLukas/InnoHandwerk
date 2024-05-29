import { Component, Input } from '@angular/core';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-chat-message',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './chat-message.component.html',
  styleUrl: './chat-message.component.css'
})
export class ChatMessageComponent {
  @Input() message: any;
}
