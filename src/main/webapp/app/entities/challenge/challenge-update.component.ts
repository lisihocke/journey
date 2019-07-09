import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Challenge, IChallenge } from 'app/shared/model/challenge.model';
import { Observable } from 'rxjs';
import { ChallengeService } from '../challenge/challenge.service';

@Component({
  selector: 'jhi-challenge-update',
  templateUrl: './challenge-update.component.html'
})
export class ChallengeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    tag: [null, [Validators.required, Validators.maxLength(100)]],
    challengeDescription: [null, [Validators.required]],
    hypothesis: [null, [Validators.required]],
    probe: [null, [Validators.required]],
    pauseCriteria: [null],
    exitCriteria: [null, [Validators.required]],
    influences: [null],
    notes: [null]
  });

  constructor(protected challengeService: ChallengeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ challenge }) => {
      this.updateForm(challenge);
    });
  }

  updateForm(challenge: IChallenge) {
    this.editForm.patchValue({
      id: challenge.id,
      tag: challenge.tag,
      challengeDescription: challenge.challengeDescription,
      hypothesis: challenge.hypothesis,
      probe: challenge.probe,
      pauseCriteria: challenge.pauseCriteria,
      exitCriteria: challenge.exitCriteria,
      influences: challenge.influences,
      notes: challenge.notes
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const challenge = this.createFromForm();
    if (challenge.id !== undefined) {
      this.subscribeToSaveResponse(this.challengeService.update(challenge));
    } else {
      this.subscribeToSaveResponse(this.challengeService.create(challenge));
    }
  }

  private createFromForm(): IChallenge {
    const entity = {
      ...new Challenge(),
      id: this.editForm.get(['id']).value,
      tag: this.editForm.get(['tag']).value,
      challengeDescription: this.editForm.get(['challengeDescription']).value,
      hypothesis: this.editForm.get(['hypothesis']).value,
      probe: this.editForm.get(['probe']).value,
      pauseCriteria: this.editForm.get(['pauseCriteria']).value,
      exitCriteria: this.editForm.get(['exitCriteria']).value,
      influences: this.editForm.get(['influences']).value,
      notes: this.editForm.get(['notes']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChallenge>>) {
    result.subscribe((res: HttpResponse<IChallenge>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
