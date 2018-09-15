import { Route } from '@angular/router';
import { ElasticsearchReindexComponent } from './elasticsearch-reindex.component';
import {UserRouteAccessService} from '../../core/auth/user-route-access-service';

export const elasticsearchReindexRoute: Route = {
    path: 'elasticsearch-reindex',
    component: ElasticsearchReindexComponent,
    data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'elasticsearch.reindex.title'
    },
    canActivate: [UserRouteAccessService]
};
