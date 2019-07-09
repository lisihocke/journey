import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'journal-entry',
        loadChildren: './journal-entry/journal-entry.module#JourneyJournalEntryModule'
      },
      {
        path: 'challenge',
        loadChildren: './challenge/challenge.module#JourneyChallengeModule'
      }
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JourneyEntityModule {}
