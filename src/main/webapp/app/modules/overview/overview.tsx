import './overview.scss';
import 'react-big-calendar/lib/css/react-big-calendar.css';

import React from 'react';
import { Redirect } from 'react-router';
import BigCalendar from 'react-big-calendar';
import dates from 'react-big-calendar/lib/utils/dates';
import moment from 'moment';

import { Translate, translate, log } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { getSession } from 'app/shared/reducers/authentication';

import { getEntitiesBetween } from 'app/entities/contract/contract.reducer';

export interface IOverviewProp extends StateProps, DispatchProps {}

export class Overview extends React.Component<IOverviewProp> {
  localizer = BigCalendar.momentLocalizer(moment);
  state = {
    eventId: null
  };

  componentDidMount() {
    this.props.getSession();
    this.getEntities(moment());
  }

  getEntities = currentDate => {
    log('currentDate: ', currentDate);
    this.props.getEntitiesBetween(
      moment(dates.firstVisibleDay(currentDate, this.localizer)),
      moment(dates.lastVisibleDay(currentDate, this.localizer))
    );
  };

  onNavigate = date => {
    this.getEntities(date);
  };

  onDoubleClick = (event, e) => {
    this.setState({
      ...this.state,
      eventId: event.id
    });
  };

  render() {
    const { locale, eventList } = this.props;

    if (this.state.eventId !== null) {
      return <Redirect push to={`/entity/contract/${this.state.eventId}`} />;
    }

    return (
      <Row>
        <Col md="9">
          <h2>
            <Translate contentKey="overview.title">Contract Overview</Translate>
          </h2>

          <div className="rbc-calendar">
            <BigCalendar
              events={eventList}
              views={['month', 'week', 'day', 'agenda']}
              localizer={this.localizer}
              messages={{
                date: translate('overview.calendar.date'),
                time: translate('overview.calendar.time'),
                event: translate('overview.calendar.event'),
                allDay: translate('overview.calendar.allDay'),
                week: translate('overview.calendar.week'),
                work_week: translate('overview.calendar.work_week'),
                day: translate('overview.calendar.week'),
                month: translate('overview.calendar.month'),
                previous: translate('overview.calendar.previous'),
                next: translate('overview.calendar.next'),
                yesterday: translate('overview.calendar.yesterday'),
                tomorrow: translate('overview.calendar.tomorrow'),
                today: translate('overview.calendar.today'),
                agenda: translate('overview.calendar.agenda'),
                showMore: count => translate('overview.calendar.showmore', { count }),
                noEventsInRange: translate('overview.calendar.noeventsinrange')
              }}
              culture={locale}
              onNavigate={this.onNavigate}
              popup
              onDoubleClickEvent={this.onDoubleClick}
            />
          </div>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  locale: storeState.locale.currentLocale,
  eventList: storeState.contract.entities.map(contract => ({
    id: contract.id,
    title: contract.name,
    start: contract.contractEnd,
    end: contract.contractEnd,
    allDay: true
  }))
});

const mapDispatchToProps = { getSession, getEntitiesBetween };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Overview);
