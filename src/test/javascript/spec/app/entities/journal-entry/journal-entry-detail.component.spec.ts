/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JourneyTestModule } from '../../../test.module';
import { JournalEntryDetailComponent } from 'app/entities/journal-entry/journal-entry-detail.component';
import { JournalEntry } from 'app/shared/model/journal-entry.model';

describe('Component Tests', () => {
  describe('JournalEntry Management Detail Component', () => {
    let comp: JournalEntryDetailComponent;
    let fixture: ComponentFixture<JournalEntryDetailComponent>;
    const route = ({ data: of({ journalEntry: new JournalEntry(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JourneyTestModule],
        declarations: [JournalEntryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JournalEntryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JournalEntryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.journalEntry).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
