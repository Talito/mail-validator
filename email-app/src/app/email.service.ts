import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EmailEncoder} from './email-encoder';
import {environment} from '../environments/environment';
import {EmailVerification} from './email-verification';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(
    private http: HttpClient
  ) {
  }

  verify(email: string): Observable<EmailVerification> {
    const params = new HttpParams({encoder: new EmailEncoder()}).set('email', email);
    return this.http.get<EmailVerification>(environment.apiBaseUrl + '/v1/email/verify', {params: params});
  }

  history(): Observable<EmailVerification[]> {
    return this.http.get<EmailVerification[]>(environment.apiBaseUrl + '/v1/email/verify/history');
  }

}
