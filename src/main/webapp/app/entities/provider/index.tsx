import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Provider from './provider';
import ProviderDetail from './provider-detail';
import ProviderUpdate from './provider-update';
import ProviderDeleteDialog from './provider-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProviderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProviderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProviderDetail} />
      <ErrorBoundaryRoute path={match.url} component={Provider} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ProviderDeleteDialog} />
  </>
);

export default Routes;
