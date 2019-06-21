/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { JourneyTestModule } from '../../../test.module';
import { JournalEntryUpdateComponent } from 'app/entities/journal-entry/journal-entry-update.component';
import { JournalEntryService } from 'app/entities/journal-entry/journal-entry.service';
import { JournalEntry } from 'app/shared/model/journal-entry.model';

describe('Component Tests', () => {
  describe('JournalEntry Management Update Component', () => {
    let comp: JournalEntryUpdateComponent;
    let fixture: ComponentFixture<JournalEntryUpdateComponent>;
    let service: JournalEntryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JourneyTestModule],
        declarations: [JournalEntryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JournalEntryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JournalEntryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JournalEntryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JournalEntry(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new JournalEntry();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
