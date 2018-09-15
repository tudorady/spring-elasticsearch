/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Es2TestModule } from '../../../test.module';
import { SubscriberDetailComponent } from 'app/entities/subscriber/subscriber-detail.component';
import { Subscriber } from 'app/shared/model/subscriber.model';

describe('Component Tests', () => {
    describe('Subscriber Management Detail Component', () => {
        let comp: SubscriberDetailComponent;
        let fixture: ComponentFixture<SubscriberDetailComponent>;
        const route = ({ data: of({ subscriber: new Subscriber(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Es2TestModule],
                declarations: [SubscriberDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SubscriberDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubscriberDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.subscriber).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
