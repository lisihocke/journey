import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { JourneySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [JourneySharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [JourneySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JourneySharedModule {
  static forRoot() {
    return {
      ngModule: JourneySharedModule
    };
  }
}
