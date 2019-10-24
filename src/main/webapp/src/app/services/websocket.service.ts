import {Injectable} from '@angular/core';
import {webSocket, WebSocketSubject} from 'rxjs/webSocket';
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  constructor(
    private authService: AuthService
  ) {
  }

  public connect(projectId: number) {
    let subject = webSocket('ws://localhost:8080/lab2-1.0-SNAPSHOT/issues');
    subject.subscribe();
    const initParams = {userId: this.authService.getUserId(), projectId: projectId};
    subject.next(JSON.stringify(initParams));

    return subject;
  }

  public disconnect(subject: WebSocketSubject<String>) {
    subject.unsubscribe();
  }
}
