import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JourneySharedModule } from 'app/shared';
import {
  JournalEntryComponent,
  JournalEntryDetailComponent,
  JournalEntryUpdateComponent,
  JournalEntryDeletePopupComponent,
  JournalEntryDeleteDialogComponent,
  journalEntryRoute,
  journalEntryPopupRoute
} from './';

const ENTITY_STATES = [...journalEntryRoute, ...journalEntryPopupRoute];

@NgModule({
  imports: [JourneySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    JournalEntryComponent,
    JournalEntryDetailComponent,
    JournalEntryUpdateComponent,
    JournalEntryDeleteDialogComponent,
    JournalEntryDeletePopupComponent
  ],
  entryComponents: [
    JournalEntryComponent,
    JournalEntryUpdateComponent,
    JournalEntryDeleteDialogComponent,
    JournalEntryDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JourneyJournalEntryModule {}
