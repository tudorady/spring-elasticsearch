import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { Es2SubscriberModule } from './subscriber/subscriber.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        Es2SubscriberModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Es2EntityModule {}
