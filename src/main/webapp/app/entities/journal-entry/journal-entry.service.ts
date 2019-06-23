import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IJournalEntry } from 'app/shared/model/journal-entry.model';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IJournalEntry>;
type EntityArrayResponseType = HttpResponse<IJournalEntry[]>;

@Injectable({ providedIn: 'root' })
export class JournalEntryService {
  public resourceUrl = SERVER_API_URL + 'api/journal-entries';

  constructor(protected http: HttpClient) {}

  create(journalEntry: IJournalEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journalEntry);
    return this.http
      .post<IJournalEntry>(this.resourceUrl, journalEntry, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(journalEntry: IJournalEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journalEntry);
    return this.http
      .put<IJournalEntry>(this.resourceUrl, journalEntry, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJournalEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJournalEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(testEntity: IJournalEntry): IJournalEntry {
    const copy: IJournalEntry = Object.assign({}, testEntity, {
      date: testEntity.date != null && testEntity.date.isValid() ? testEntity.date.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((testEntity: IJournalEntry) => {
        testEntity.date = testEntity.date != null ? moment(testEntity.date) : null;
      });
    }
    return res;
  }
}
