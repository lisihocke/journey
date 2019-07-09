import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IChallenge } from 'app/shared/model/challenge.model';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IChallenge>;
type EntityArrayResponseType = HttpResponse<IChallenge[]>;

@Injectable({ providedIn: 'root' })
export class ChallengeService {
  public resourceUrl = SERVER_API_URL + 'api/challenges';

  constructor(protected http: HttpClient) {}

  create(challenge: IChallenge): Observable<EntityResponseType> {
    return this.http.post<IChallenge>(this.resourceUrl, challenge, { observe: 'response' });
  }

  update(challenge: IChallenge): Observable<EntityResponseType> {
    return this.http.put<IChallenge>(this.resourceUrl, challenge, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChallenge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChallenge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
