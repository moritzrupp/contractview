import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProvider } from 'app/shared/model/provider.model';
import { getEntities as getProviders } from 'app/entities/provider/provider.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './contract.reducer';
import { IContract } from 'app/shared/model/contract.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContractUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IContractUpdateState {
  isNew: boolean;
  idsusers: any[];
  providerId: string;
  ownerId: string;
}

export class ContractUpdate extends React.Component<IContractUpdateProps, IContractUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsusers: [],
      providerId: '0',
      ownerId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getProviders();
    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    values.contractStart = new Date(values.contractStart);
    values.contractEnd = new Date(values.contractEnd);

    if (errors.length === 0) {
      const { contractEntity } = this.props;
      const entity = {
        ...contractEntity,
        ...values,
        users: mapIdList(values.users)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/contract');
  };

  render() {
    const { contractEntity, providers, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="contractviewApp.contract.home.createOrEditLabel">
              <Translate contentKey="contractviewApp.contract.home.createOrEditLabel">Create or edit a Contract</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : contractEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="contract-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="contractviewApp.contract.name">Name</Translate>
                  </Label>
                  <AvField
                    id="contract-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="contactEmailLabel" for="contactEmail">
                    <Translate contentKey="contractviewApp.contract.contactEmail">Contact Email</Translate>
                  </Label>
                  <AvField id="contract-contactEmail" type="text" name="contactEmail" />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    <Translate contentKey="contractviewApp.contract.price">Price</Translate>
                  </Label>
                  <AvField
                    id="contract-price"
                    type="string"
                    className="form-control"
                    name="price"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="billingPeriodDaysLabel" for="billingPeriodDays">
                    <Translate contentKey="contractviewApp.contract.billingPeriodDays">Billing Period Days</Translate>
                  </Label>
                  <AvField
                    id="contract-billingPeriodDays"
                    type="string"
                    className="form-control"
                    name="billingPeriodDays"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="contractStartLabel" for="contractStart">
                    <Translate contentKey="contractviewApp.contract.contractStart">Contract Start</Translate>
                  </Label>
                  <AvInput
                    id="contract-contractStart"
                    type="datetime-local"
                    className="form-control"
                    name="contractStart"
                    value={isNew ? null : convertDateTimeFromServer(this.props.contractEntity.contractStart)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="contractEndLabel" for="contractEnd">
                    <Translate contentKey="contractviewApp.contract.contractEnd">Contract End</Translate>
                  </Label>
                  <AvInput
                    id="contract-contractEnd"
                    type="datetime-local"
                    className="form-control"
                    name="contractEnd"
                    value={isNew ? null : convertDateTimeFromServer(this.props.contractEntity.contractEnd)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="automaticExtensionLabel" check>
                    <AvInput id="contract-automaticExtension" type="checkbox" className="form-control" name="automaticExtension" />
                    <Translate contentKey="contractviewApp.contract.automaticExtension">Automatic Extension</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="extensionPeriodDaysLabel" for="extensionPeriodDays">
                    <Translate contentKey="contractviewApp.contract.extensionPeriodDays">Extension Period Days</Translate>
                  </Label>
                  <AvField
                    id="contract-extensionPeriodDays"
                    type="string"
                    className="form-control"
                    name="extensionPeriodDays"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="extensionReminderLabel" check>
                    <AvInput id="contract-extensionReminder" type="checkbox" className="form-control" name="extensionReminder" />
                    <Translate contentKey="contractviewApp.contract.extensionReminder">Extension Reminder</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="extensionReminderPeriodDaysLabel" for="extensionReminderPeriodDays">
                    <Translate contentKey="contractviewApp.contract.extensionReminderPeriodDays">Extension Reminder Period Days</Translate>
                  </Label>
                  <AvField
                    id="contract-extensionReminderPeriodDays"
                    type="string"
                    className="form-control"
                    name="extensionReminderPeriodDays"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="provider.name">
                    <Translate contentKey="contractviewApp.contract.provider">Provider</Translate>
                  </Label>
                  <AvInput
                    id="contract-provider"
                    type="select"
                    className="form-control"
                    name="provider.id"
                    value={isNew ? providers[0] && providers[0].id : contractEntity.provider.id}
                  >
                    {providers
                      ? providers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="owner.login">
                    <Translate contentKey="contractviewApp.contract.owner">Owner</Translate>
                  </Label>
                  <AvInput
                    id="contract-owner"
                    type="select"
                    className="form-control"
                    name="owner.id"
                    value={isNew ? users[0] && users[0].id : contractEntity.owner.id}
                  >
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="users">
                    <Translate contentKey="contractviewApp.contract.users">Users</Translate>
                  </Label>
                  <AvInput
                    id="contract-users"
                    type="select"
                    multiple
                    className="form-control"
                    name="users"
                    value={contractEntity.users && contractEntity.users.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/contract" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  providers: storeState.provider.entities,
  users: storeState.userManagement.users,
  contractEntity: storeState.contract.entity,
  loading: storeState.contract.loading,
  updating: storeState.contract.updating,
  updateSuccess: storeState.contract.updateSuccess
});

const mapDispatchToProps = {
  getProviders,
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContractUpdate);
