import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IJournalEntry } from 'app/shared/model/journal-entry.model';

type EntityResponseType = HttpResponse<IJournalEntry>;
type EntityArrayResponseType = HttpResponse<IJournalEntry[]>;

@Injectable({ providedIn: 'root' })
export class JournalEntryService {
  public resourceUrl = SERVER_API_URL + 'api/journal-entries';

  constructor(protected http: HttpClient) {}

  create(journalEntry: IJournalEntry): Observable<EntityResponseType> {
    return this.http.post<IJournalEntry>(this.resourceUrl, journalEntry, { observe: 'response' });
  }

  update(journalEntry: IJournalEntry): Observable<EntityResponseType> {
    return this.http.put<IJournalEntry>(this.resourceUrl, journalEntry, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJournalEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJournalEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
