/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { ChallengeDetailComponent } from 'app/entities/challenge';
import { Challenge } from 'app/shared/model/challenge.model';
import { of } from 'rxjs';
import { JourneyTestModule } from '../../../test.module';

describe('Component Tests', () => {
  describe('Challenge Management Detail Component', () => {
    let comp: ChallengeDetailComponent;
    let fixture: ComponentFixture<ChallengeDetailComponent>;
    const route = ({ data: of({ challenge: new Challenge(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JourneyTestModule],
        declarations: [ChallengeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChallengeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChallengeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.challenge).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
