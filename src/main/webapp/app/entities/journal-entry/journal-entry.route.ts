import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JournalEntry } from 'app/shared/model/journal-entry.model';
import { JournalEntryService } from './journal-entry.service';
import { JournalEntryComponent } from './journal-entry.component';
import { JournalEntryDetailComponent } from './journal-entry-detail.component';
import { JournalEntryUpdateComponent } from './journal-entry-update.component';
import { JournalEntryDeletePopupComponent } from './journal-entry-delete-dialog.component';
import { IJournalEntry } from 'app/shared/model/journal-entry.model';

@Injectable({ providedIn: 'root' })
export class JournalEntryResolve implements Resolve<IJournalEntry> {
  constructor(private service: JournalEntryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IJournalEntry> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<JournalEntry>) => response.ok),
        map((journalEntry: HttpResponse<JournalEntry>) => journalEntry.body)
      );
    }
    return of(new JournalEntry());
  }
}

export const journalEntryRoute: Routes = [
  {
    path: '',
    component: JournalEntryComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'date,desc',
      pageTitle: 'JournalEntries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JournalEntryDetailComponent,
    resolve: {
      journalEntry: JournalEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JournalEntries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JournalEntryUpdateComponent,
    resolve: {
      journalEntry: JournalEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JournalEntries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JournalEntryUpdateComponent,
    resolve: {
      journalEntry: JournalEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JournalEntries'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const journalEntryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: JournalEntryDeletePopupComponent,
    resolve: {
      journalEntry: JournalEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'JournalEntries'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
