import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './contract.reducer';
import { IContract } from 'app/shared/model/contract.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IContractProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IContractState = IPaginationBaseState;

export class Contract extends React.Component<IContractProps, IContractState> {
  state: IContractState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { contractList, match } = this.props;
    return (
      <div>
        <h2 id="contract-heading">
          <Translate contentKey="contractviewApp.contract.home.title">Contracts</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="contractviewApp.contract.home.createLabel">Create a new Contract</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {contractList && contractList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('name')}>
                      <Translate contentKey="contractviewApp.contract.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('contactEmail')}>
                      <Translate contentKey="contractviewApp.contract.contactEmail">Contact Email</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('price')}>
                      <Translate contentKey="contractviewApp.contract.price">Price</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('billingPeriodDays')}>
                      <Translate contentKey="contractviewApp.contract.billingPeriodDays">Billing Period Days</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('contractStart')}>
                      <Translate contentKey="contractviewApp.contract.contractStart">Contract Start</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('contractEnd')}>
                      <Translate contentKey="contractviewApp.contract.contractEnd">Contract End</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('automaticExtension')}>
                      <Translate contentKey="contractviewApp.contract.automaticExtension">Automatic Extension</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('extensionPeriodDays')}>
                      <Translate contentKey="contractviewApp.contract.extensionPeriodDays">Extension Period Days</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('extensionReminder')}>
                      <Translate contentKey="contractviewApp.contract.extensionReminder">Extension Reminder</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('extensionReminderPeriodDays')}>
                      <Translate contentKey="contractviewApp.contract.extensionReminderPeriodDays">
                        Extension Reminder Period Days
                      </Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="contractviewApp.contract.provider">Provider</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="contractviewApp.contract.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {contractList.map((contract, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${contract.id}`} color="link" size="sm">
                          {contract.id}
                        </Button>
                      </td>
                      <td>{contract.name}</td>
                      <td>{contract.contactEmail}</td>
                      <td>{contract.price}</td>
                      <td>{contract.billingPeriodDays}</td>
                      <td>
                        <TextFormat type="date" value={contract.contractStart} format={APP_DATE_FORMAT} />
                      </td>
                      <td>
                        <TextFormat type="date" value={contract.contractEnd} format={APP_DATE_FORMAT} />
                      </td>
                      <td>{contract.automaticExtension ? 'true' : 'false'}</td>
                      <td>{contract.extensionPeriodDays}</td>
                      <td>{contract.extensionReminder ? 'true' : 'false'}</td>
                      <td>{contract.extensionReminderPeriodDays}</td>
                      <td>{contract.provider ? <Link to={`provider/${contract.provider.id}`}>{contract.provider.name}</Link> : ''}</td>
                      <td>{contract.owner ? contract.owner.login : ''}</td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${contract.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.view">View</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${contract.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.edit">Edit</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${contract.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.delete">Delete</Translate>
                            </span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">
                <Translate contentKey="contractviewApp.contract.home.notFound">No Contracts found</Translate>
              </div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ contract }: IRootState) => ({
  contractList: contract.entities,
  totalItems: contract.totalItems,
  links: contract.links,
  entity: contract.entity,
  updateSuccess: contract.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Contract);
