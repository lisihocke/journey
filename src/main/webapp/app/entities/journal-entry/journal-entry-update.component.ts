import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IJournalEntry, JournalEntry } from 'app/shared/model/journal-entry.model';
import { JournalEntryService } from './journal-entry.service';

@Component({
  selector: 'jhi-journal-entry-update',
  templateUrl: './journal-entry-update.component.html'
})
export class JournalEntryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(255)]],
    description: [null, [Validators.maxLength(8000)]]
  });

  constructor(protected journalEntryService: JournalEntryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ journalEntry }) => {
      this.updateForm(journalEntry);
    });
  }

  updateForm(journalEntry: IJournalEntry) {
    this.editForm.patchValue({
      id: journalEntry.id,
      title: journalEntry.title,
      description: journalEntry.description
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const journalEntry = this.createFromForm();
    if (journalEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.journalEntryService.update(journalEntry));
    } else {
      this.subscribeToSaveResponse(this.journalEntryService.create(journalEntry));
    }
  }

  private createFromForm(): IJournalEntry {
    const entity = {
      ...new JournalEntry(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      description: this.editForm.get(['description']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJournalEntry>>) {
    result.subscribe((res: HttpResponse<IJournalEntry>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
