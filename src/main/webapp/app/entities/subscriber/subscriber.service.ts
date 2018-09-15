import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISubscriber } from 'app/shared/model/subscriber.model';

type EntityResponseType = HttpResponse<ISubscriber>;
type EntityArrayResponseType = HttpResponse<ISubscriber[]>;

@Injectable({ providedIn: 'root' })
export class SubscriberService {
    private resourceUrl = SERVER_API_URL + 'api/subscribers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/subscribers';

    constructor(private http: HttpClient) {}

    create(subscriber: ISubscriber): Observable<EntityResponseType> {
        return this.http.post<ISubscriber>(this.resourceUrl, subscriber, { observe: 'response' });
    }

    update(subscriber: ISubscriber): Observable<EntityResponseType> {
        return this.http.put<ISubscriber>(this.resourceUrl, subscriber, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISubscriber>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISubscriber[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISubscriber[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
