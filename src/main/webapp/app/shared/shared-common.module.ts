import { NgModule } from '@angular/core';

import { JourneySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [JourneySharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [JourneySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class JourneySharedCommonModule {}
