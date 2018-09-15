import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Es2SharedModule } from 'app/shared';
import {
    SubscriberComponent,
    SubscriberDetailComponent,
    SubscriberUpdateComponent,
    SubscriberDeletePopupComponent,
    SubscriberDeleteDialogComponent,
    subscriberRoute,
    subscriberPopupRoute
} from './';

const ENTITY_STATES = [...subscriberRoute, ...subscriberPopupRoute];

@NgModule({
    imports: [Es2SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SubscriberComponent,
        SubscriberDetailComponent,
        SubscriberUpdateComponent,
        SubscriberDeleteDialogComponent,
        SubscriberDeletePopupComponent
    ],
    entryComponents: [SubscriberComponent, SubscriberUpdateComponent, SubscriberDeleteDialogComponent, SubscriberDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Es2SubscriberModule {}
