/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JourneyTestModule } from '../../../test.module';
import { JournalEntryDeleteDialogComponent } from 'app/entities/journal-entry/journal-entry-delete-dialog.component';
import { JournalEntryService } from 'app/entities/journal-entry/journal-entry.service';

describe('Component Tests', () => {
  describe('JournalEntry Management Delete Component', () => {
    let comp: JournalEntryDeleteDialogComponent;
    let fixture: ComponentFixture<JournalEntryDeleteDialogComponent>;
    let service: JournalEntryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JourneyTestModule],
        declarations: [JournalEntryDeleteDialogComponent]
      })
        .overrideTemplate(JournalEntryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JournalEntryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JournalEntryService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
