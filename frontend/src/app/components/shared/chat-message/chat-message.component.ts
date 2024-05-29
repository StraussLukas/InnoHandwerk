import { Component, Input } from '@angular/core';
import {DatePipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-chat-message',
  standalone: true,
  imports: [DatePipe, NgIf],
  templateUrl: './chat-message.component.html',
  styleUrls: ['./chat-message.component.css']
})
export class ChatMessageComponent {
  @Input() message: any;
  currentImageIndex: number = 0;

  prevImage() {
    if (this.message.images && this.message.images.length) {
      this.currentImageIndex = (this.currentImageIndex - 1 + this.message.images.length) % this.message.images.length;
    }
  }

  nextImage() {
    if (this.message.images && this.message.images.length) {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.message.images.length;
    }
  }
}
