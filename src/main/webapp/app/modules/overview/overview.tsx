import './overview.scss';
import 'react-big-calendar/lib/css/react-big-calendar.css';

import React from 'react';
import { Redirect } from 'react-router';
import BigCalendar from 'react-big-calendar';
import dates from 'react-big-calendar/lib/utils/dates';
import moment from 'moment';

import { Translate, translate, log } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Popover, PopoverHeader, PopoverBody } from 'reactstrap';

import { getSession } from 'app/shared/reducers/authentication';

import { getEntitiesBetween } from 'app/entities/contract/contract.reducer';

export interface IOverviewProp extends StateProps, DispatchProps {}

const defaultEventPopover = {
  popoverTarget: null,
  eventTitle: null,
  eventProvider: null,
  eventStart: null,
  eventEnd: null
};

export class Overview extends React.Component<IOverviewProp> {
  localizer = BigCalendar.momentLocalizer(moment);

  state = {
    eventId: null,
    eventPopover: defaultEventPopover
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

  onSelectEvent = (event, e) => {
    if (e.currentTarget === this.state.eventPopover.popoverTarget) {
      this.setState({
        ...this.state,
        eventPopover: defaultEventPopover
      });
    } else {
      this.setState({
        ...this.state,
        eventPopover: {
          popoverTarget: e.currentTarget,
          eventTitle: event.title,
          eventProvider: event.provider,
          eventStart: event.start,
          eventEnd: event.end
        }
      });
    }
  };

  render() {
    const { locale, eventList } = this.props;
    moment.locale(locale);

    if (this.state.eventId !== null) {
      return <Redirect push to={`/entity/contract/${this.state.eventId}`} />;
    }

    let popover;

    if (this.state.eventPopover.popoverTarget !== null) {
      popover = (
        <Popover placement={'top'} isOpen target={this.state.eventPopover.popoverTarget}>
          <PopoverHeader>{this.state.eventPopover.eventTitle}</PopoverHeader>
          <PopoverBody>
            <Row>
              <Col>{this.state.eventPopover.eventProvider}</Col>
            </Row>
            <Row>
              <Col>
                {moment(this.state.eventPopover.eventStart).format('LL')}-{moment(this.state.eventPopover.eventEnd).format('LL')}
              </Col>
            </Row>
          </PopoverBody>
        </Popover>
      );
    }

    return (
      <Row>
        <Col md="9">
          <h2>
            <Translate contentKey="overview.title">Contract Overview</Translate>
          </h2>

          {popover}
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
              onSelectEvent={this.onSelectEvent}
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
    provider: contract.provider.name,
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
