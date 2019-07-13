/* tslint:disable max-line-length */
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { getTestBed, TestBed } from '@angular/core/testing';
import { ChallengeService } from 'app/entities/challenge';
import { Challenge, IChallenge } from 'app/shared/model/challenge.model';
import { map, take } from 'rxjs/operators';

describe('Service Tests', () => {
  describe('Challenge Service', () => {
    let injector: TestBed;
    let service: ChallengeService;
    let httpMock: HttpTestingController;
    let elemDefault: IChallenge;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ChallengeService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Challenge(
        0,
        'tag',
        'challengeDescription',
        'hypothesis',
        'probe',
        'pauseCriteria',
        'exitCriteria',
        'influences',
        'notes'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Challenge', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new Challenge(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Challenge', async () => {
        const returnedFromService = Object.assign(
          {
            tag: 'tag2',
            challengeDescription: 'challengeDescription2',
            hypothesis: 'hypothesis2',
            probe: 'probe2',
            pauseCriteria: 'pauseCriteria2',
            exitCriteria: 'exitCriteria2',
            influences: 'influences2',
            notes: 'notes2'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of challenges', async () => {
        const returnedFromService = Object.assign(
          {
            tag: 'tag',
            challengeDescription: 'challengeDescription',
            hypothesis: 'hypothesis',
            probe: 'probe',
            pauseCriteria: 'pauseCriteria',
            exitCriteria: 'exitCriteria',
            influences: 'influences',
            notes: 'notes'
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Challenge', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
