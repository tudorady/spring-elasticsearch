import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Subscriber } from 'app/shared/model/subscriber.model';
import { SubscriberService } from './subscriber.service';
import { SubscriberComponent } from './subscriber.component';
import { SubscriberDetailComponent } from './subscriber-detail.component';
import { SubscriberUpdateComponent } from './subscriber-update.component';
import { SubscriberDeletePopupComponent } from './subscriber-delete-dialog.component';
import { ISubscriber } from 'app/shared/model/subscriber.model';

@Injectable({ providedIn: 'root' })
export class SubscriberResolve implements Resolve<ISubscriber> {
    constructor(private service: SubscriberService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((subscriber: HttpResponse<Subscriber>) => subscriber.body));
        }
        return of(new Subscriber());
    }
}

export const subscriberRoute: Routes = [
    {
        path: 'subscriber',
        component: SubscriberComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'es2App.subscriber.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subscriber/:id/view',
        component: SubscriberDetailComponent,
        resolve: {
            subscriber: SubscriberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'es2App.subscriber.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subscriber/new',
        component: SubscriberUpdateComponent,
        resolve: {
            subscriber: SubscriberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'es2App.subscriber.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subscriber/:id/edit',
        component: SubscriberUpdateComponent,
        resolve: {
            subscriber: SubscriberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'es2App.subscriber.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subscriberPopupRoute: Routes = [
    {
        path: 'subscriber/:id/delete',
        component: SubscriberDeletePopupComponent,
        resolve: {
            subscriber: SubscriberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'es2App.subscriber.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
