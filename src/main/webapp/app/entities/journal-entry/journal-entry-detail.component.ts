import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJournalEntry } from 'app/shared/model/journal-entry.model';

@Component({
  selector: 'jhi-journal-entry-detail',
  templateUrl: './journal-entry-detail.component.html'
})
export class JournalEntryDetailComponent implements OnInit {
  journalEntry: IJournalEntry;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ journalEntry }) => {
      this.journalEntry = journalEntry;
    });
  }

  previousState() {
    window.history.back();
  }
}
