import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubscriber } from 'app/shared/model/subscriber.model';

@Component({
    selector: 'jhi-subscriber-detail',
    templateUrl: './subscriber-detail.component.html'
})
export class SubscriberDetailComponent implements OnInit {
    subscriber: ISubscriber;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ subscriber }) => {
            this.subscriber = subscriber;
        });
    }

    previousState() {
        window.history.back();
    }
}
