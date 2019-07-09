import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IChallenge } from 'app/shared/model/challenge.model';
import { IJournalEntry, JournalEntry } from 'app/shared/model/journal-entry.model';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChallengeService } from '../challenge/challenge.service';
import { JournalEntryService } from './journal-entry.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-journal-entry-update',
  templateUrl: './journal-entry-update.component.html'
})
export class JournalEntryUpdateComponent implements OnInit {
  isSaving: boolean;
  dateDp: any;
  challenges: IChallenge[];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    title: [null, [Validators.required, Validators.maxLength(255)]],
    description: [null, [Validators.maxLength(8000)]],
    challengeId: [null, [Validators.required]]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected journalEntryService: JournalEntryService,
    protected challengeService: ChallengeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ journalEntry }) => {
      this.updateForm(journalEntry);
    });
    this.challengeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChallenge[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChallenge[]>) => response.body)
      )
      .subscribe((res: IChallenge[]) => (this.challenges = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(journalEntry: IJournalEntry) {
    this.editForm.patchValue({
      id: journalEntry.id,
      date: journalEntry.date,
      title: journalEntry.title,
      description: journalEntry.description,
      challengeId: journalEntry.challengeId
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
      date: this.editForm.get(['date']).value,
      title: this.editForm.get(['title']).value,
      description: this.editForm.get(['description']).value,
      challengeId: this.editForm.get(['challengeId']).value
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

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackChallengeById(index: number, item: IChallenge) {
    return item.id;
  }
}
