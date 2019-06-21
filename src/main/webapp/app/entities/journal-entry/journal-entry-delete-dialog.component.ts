import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJournalEntry } from 'app/shared/model/journal-entry.model';
import { JournalEntryService } from './journal-entry.service';

@Component({
  selector: 'jhi-journal-entry-delete-dialog',
  templateUrl: './journal-entry-delete-dialog.component.html'
})
export class JournalEntryDeleteDialogComponent {
  journalEntry: IJournalEntry;

  constructor(
    protected journalEntryService: JournalEntryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.journalEntryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'journalEntryListModification',
        content: 'Deleted an journalEntry'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-journal-entry-delete-popup',
  template: ''
})
export class JournalEntryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ journalEntry }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(JournalEntryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.journalEntry = journalEntry;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/journal-entry', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/journal-entry', { outlets: { popup: null } }]);
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
