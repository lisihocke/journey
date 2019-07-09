import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IChallenge } from 'app/shared/model/challenge.model';

@Component({
  selector: 'jhi-challenge-detail',
  templateUrl: './challenge-detail.component.html'
})
export class ChallengeDetailComponent implements OnInit {
  challenge: IChallenge;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ challenge }) => {
      this.challenge = challenge;
    });
  }

  previousState() {
    window.history.back();
  }
}
