/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Es2TestModule } from '../../../test.module';
import { SubscriberUpdateComponent } from 'app/entities/subscriber/subscriber-update.component';
import { SubscriberService } from 'app/entities/subscriber/subscriber.service';
import { Subscriber } from 'app/shared/model/subscriber.model';

describe('Component Tests', () => {
    describe('Subscriber Management Update Component', () => {
        let comp: SubscriberUpdateComponent;
        let fixture: ComponentFixture<SubscriberUpdateComponent>;
        let service: SubscriberService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Es2TestModule],
                declarations: [SubscriberUpdateComponent]
            })
                .overrideTemplate(SubscriberUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SubscriberUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubscriberService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Subscriber(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.subscriber = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Subscriber();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.subscriber = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
