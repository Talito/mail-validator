import {Component, OnInit} from '@angular/core';
import {EmailService} from '../email.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {

  apiIsAccessible: boolean;
  constructor(
    private emailService: EmailService
  ) {
  }

  verify(email: string) {
    this.emailService.verify(email)
      .subscribe(x => {
        this.apiIsAccessible = true;
        this.emailService.requestEmailVerifications()
      }, x => { this.apiIsAccessible = false; });
  }

  ngOnInit(): void {
    this.emailService.history()
      .subscribe(x => this.emailService.requestEmailVerifications());
  }
}
