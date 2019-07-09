import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JourneySharedModule } from 'app/shared';
import {
  ChallengeComponent,
  ChallengeDeleteDialogComponent,
  ChallengeDeletePopupComponent,
  ChallengeDetailComponent,
  challengePopupRoute,
  challengeRoute,
  ChallengeUpdateComponent
} from './';

const ENTITY_STATES = [...challengeRoute, ...challengePopupRoute];

@NgModule({
  imports: [JourneySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChallengeComponent,
    ChallengeDetailComponent,
    ChallengeUpdateComponent,
    ChallengeDeleteDialogComponent,
    ChallengeDeletePopupComponent
  ],
  entryComponents: [ChallengeComponent, ChallengeUpdateComponent, ChallengeDeleteDialogComponent, ChallengeDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JourneyChallengeModule {}
