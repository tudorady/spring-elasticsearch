import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISubscriber } from 'app/shared/model/subscriber.model';
import { SubscriberService } from './subscriber.service';

@Component({
    selector: 'jhi-subscriber-update',
    templateUrl: './subscriber-update.component.html'
})
export class SubscriberUpdateComponent implements OnInit {
    private _subscriber: ISubscriber;
    isSaving: boolean;

    constructor(private subscriberService: SubscriberService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ subscriber }) => {
            this.subscriber = subscriber;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.subscriber.id !== undefined) {
            this.subscribeToSaveResponse(this.subscriberService.update(this.subscriber));
        } else {
            this.subscribeToSaveResponse(this.subscriberService.create(this.subscriber));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISubscriber>>) {
        result.subscribe((res: HttpResponse<ISubscriber>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get subscriber() {
        return this._subscriber;
    }

    set subscriber(subscriber: ISubscriber) {
        this._subscriber = subscriber;
    }
}
