import {Component, OnInit} from '@angular/core';
import {EmailService} from '../email.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {

  constructor(
    private emailService: EmailService
  ) {
  }

  ngOnInit() {
  }

  verify(email: string) {
    // TODO after verification email should appear in history instead of opening an alert
    this.emailService.verify(email)
      .subscribe(verification => {
        alert(verification.status);
      });
  }
}
