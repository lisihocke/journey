import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IChallenge } from 'app/shared/model/challenge.model';
import { JhiEventManager } from 'ng-jhipster';
import { ChallengeService } from './challenge.service';

@Component({
  selector: 'jhi-challenge-delete-dialog',
  templateUrl: './challenge-delete-dialog.component.html'
})
export class ChallengeDeleteDialogComponent {
  challenge: IChallenge;

  constructor(protected challengeService: ChallengeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.challengeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'challengeListModification',
        content: 'Deleted a challenge'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-challenge-delete-popup',
  template: ''
})
export class ChallengeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ challenge }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChallengeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.challenge = challenge;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/challenge', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/challenge', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
