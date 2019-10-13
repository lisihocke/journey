import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Challenge, IChallenge } from 'app/shared/model/challenge.model';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChallengeDeletePopupComponent } from './challenge-delete-dialog.component';
import { ChallengeDetailComponent } from './challenge-detail.component';
import { ChallengeUpdateComponent } from './challenge-update.component';
import { ChallengeComponent } from './challenge.component';
import { ChallengeService } from './challenge.service';

@Injectable({ providedIn: 'root' })
export class ChallengeResolve implements Resolve<IChallenge> {
  constructor(private service: ChallengeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChallenge> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Challenge>) => response.ok),
        map((challenge: HttpResponse<Challenge>) => challenge.body)
      );
    }
    return of(new Challenge());
  }
}

export const challengeRoute: Routes = [
  {
    path: '',
    component: ChallengeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,desc',
      pageTitle: 'Challenges'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChallengeDetailComponent,
    resolve: {
      challenge: ChallengeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Challenges'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChallengeUpdateComponent,
    resolve: {
      challenge: ChallengeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Challenges'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChallengeUpdateComponent,
    resolve: {
      challenge: ChallengeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Challenges'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const challengePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChallengeDeletePopupComponent,
    resolve: {
      challenge: ChallengeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Challenges'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
