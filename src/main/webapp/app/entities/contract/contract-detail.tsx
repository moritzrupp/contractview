import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contract.reducer';
import { IContract } from 'app/shared/model/contract.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContractDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ContractDetail extends React.Component<IContractDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contractEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="contractviewApp.contract.detail.title">Contract</Translate> [<b>{contractEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="contractviewApp.contract.name">Name</Translate>
              </span>
            </dt>
            <dd>{contractEntity.name}</dd>
            <dt>
              <span id="contactEmail">
                <Translate contentKey="contractviewApp.contract.contactEmail">Contact Email</Translate>
              </span>
            </dt>
            <dd>{contractEntity.contactEmail}</dd>
            <dt>
              <span id="price">
                <Translate contentKey="contractviewApp.contract.price">Price</Translate>
              </span>
            </dt>
            <dd>{contractEntity.price}</dd>
            <dt>
              <span id="billingPeriodDays">
                <Translate contentKey="contractviewApp.contract.billingPeriodDays">Billing Period Days</Translate>
              </span>
            </dt>
            <dd>{contractEntity.billingPeriodDays}</dd>
            <dt>
              <span id="contractStart">
                <Translate contentKey="contractviewApp.contract.contractStart">Contract Start</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={contractEntity.contractStart} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="contractEnd">
                <Translate contentKey="contractviewApp.contract.contractEnd">Contract End</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={contractEntity.contractEnd} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="automaticExtension">
                <Translate contentKey="contractviewApp.contract.automaticExtension">Automatic Extension</Translate>
              </span>
            </dt>
            <dd>{contractEntity.automaticExtension ? 'true' : 'false'}</dd>
            <dt>
              <span id="extensionPeriodDays">
                <Translate contentKey="contractviewApp.contract.extensionPeriodDays">Extension Period Days</Translate>
              </span>
            </dt>
            <dd>{contractEntity.extensionPeriodDays}</dd>
            <dt>
              <span id="extensionReminder">
                <Translate contentKey="contractviewApp.contract.extensionReminder">Extension Reminder</Translate>
              </span>
            </dt>
            <dd>{contractEntity.extensionReminder ? 'true' : 'false'}</dd>
            <dt>
              <span id="extensionReminderPeriodDays">
                <Translate contentKey="contractviewApp.contract.extensionReminderPeriodDays">Extension Reminder Period Days</Translate>
              </span>
            </dt>
            <dd>{contractEntity.extensionReminderPeriodDays}</dd>
            <dt>
              <Translate contentKey="contractviewApp.contract.provider">Provider</Translate>
            </dt>
            <dd>{contractEntity.provider ? contractEntity.provider.name : ''}</dd>
            <dt>
              <Translate contentKey="contractviewApp.contract.owner">Owner</Translate>
            </dt>
            <dd>{contractEntity.owner ? contractEntity.owner.login : ''}</dd>
            <dt>
              <Translate contentKey="contractviewApp.contract.users">Users</Translate>
            </dt>
            <dd>
              {contractEntity.users
                ? contractEntity.users.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.login}</a>
                      {i === contractEntity.users.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}{' '}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/contract" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/contract/${contractEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ contract }: IRootState) => ({
  contractEntity: contract.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContractDetail);
