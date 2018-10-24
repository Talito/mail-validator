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
    // TODO css
  }

  ngOnInit(): void {
    // TODO interval based refresh should not be triggered if list was refreshed in last 10 seconds (rxjs)
    this.emailService.history()
      .subscribe(history => this.history = history);
  }

  public changeListener(files: FileList){
    console.log(files);
    if(files && files.length > 0) {
      let file : File = files.item(0);
      console.log(file.name);
      console.log(file.size);
      console.log(file.type);
      let reader: FileReader = new FileReader();
      reader.readAsText(file);
      reader.onload = (e) => {
        let csv: string = reader.result;
        console.log(csv);
      }
    }
  }
}
