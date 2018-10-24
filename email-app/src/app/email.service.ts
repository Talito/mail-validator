import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, Subject, timer} from 'rxjs';
import {environment} from '../environments/environment';
import {EmailVerification} from './email-verification';
import {map, shareReplay, switchMap} from "rxjs/operators";
import {EmailEncoder} from "./email-encoder";

const REFRESH_INTERVAL = 10000;
const CACHE_SIZE = 1;

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private cache$: Observable<Array<EmailVerification>>;
  private API_EMAIL_VERIFY_ENDPOINT = environment.apiBaseUrl + '/v1/email/verify';
  private API_EMAIL_VERIFY_HISTORY_ENDPOINT = environment.apiBaseUrl + '/v1/email/verify/history';

  constructor(
    private http: HttpClient
  ) {
  }

  verify(email: string): Observable<Array<EmailVerification>> {
    const params = new HttpParams({encoder: new EmailEncoder()}).set('email', email);
    return this.http.get<EmailVerification[]>(this.API_EMAIL_VERIFY_ENDPOINT, {params: params});
  }

  history(): Observable<Array<EmailVerification>> {
    const timer$ = timer(0, REFRESH_INTERVAL);
    if (!this.cache$) {
      this.cache$ = timer$.pipe(
        switchMap(_ => this.requestEmailVerifications()),
        shareReplay(CACHE_SIZE));
    }
    return this.cache$;
  }

  requestEmailVerifications() {
    return this.http.get<Array<EmailVerification>>(this.API_EMAIL_VERIFY_HISTORY_ENDPOINT).pipe(
      map(response => response)
    );
  }

}
