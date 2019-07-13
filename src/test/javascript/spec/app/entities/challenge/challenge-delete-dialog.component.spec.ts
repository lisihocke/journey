/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ChallengeDeleteDialogComponent, ChallengeService } from 'app/entities/challenge';
import { JhiEventManager } from 'ng-jhipster';
import { of } from 'rxjs';
import { JourneyTestModule } from '../../../test.module';

describe('Component Tests', () => {
  describe('Challenge Management Delete Component', () => {
    let comp: ChallengeDeleteDialogComponent;
    let fixture: ComponentFixture<ChallengeDeleteDialogComponent>;
    let service: ChallengeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JourneyTestModule],
        declarations: [ChallengeDeleteDialogComponent]
      })
        .overrideTemplate(ChallengeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChallengeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChallengeService);
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
