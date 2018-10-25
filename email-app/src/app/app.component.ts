import {Component, OnInit} from '@angular/core';
import {EmailVerification} from './email-verification';
import {EmailService} from './email.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  history: Array<EmailVerification>;

  constructor(
    private emailService: EmailService
  ) {
  }

  ngOnInit(): void {
    // TODO interval based refresh should not be triggered if list was refreshed in last 10 seconds (rxjs)
    this.emailService.history()
      .subscribe(history => this.history = history);
  }

}
