import './overview.scss';
import 'react-big-calendar/lib/css/react-big-calendar.css';

import React from 'react';
import { Redirect } from 'react-router';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import { firstVisibleDay, lastVisibleDay } from 'react-big-calendar/lib/utils/dates';
import moment from 'moment';

import { Translate, translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Col, Popover, PopoverBody, PopoverHeader, Row } from 'reactstrap';

import { getSession } from 'app/shared/reducers/authentication';
import { getEntities } from 'app/entities/contract/event.reducer';
import { IRootState } from 'app/shared/reducers';

export interface IOverviewProp extends StateProps, DispatchProps {}

const defaultEventPopover = {
  popoverTarget: null,
  eventTitle: null,
  eventProvider: null,
  eventStart: null,
  eventEnd: null
};

export class Overview extends React.Component<IOverviewProp> {
  localizer = momentLocalizer(moment);

  state = {
    eventId: null,
    eventPopover: defaultEventPopover
  };

  componentDidMount() {
    this.props.getSession();
    this.getEntities(new Date());
  }

  getEntities = (currentDate: Date) => {
    this.props.getEntities(moment(firstVisibleDay(currentDate, this.localizer)), moment(lastVisibleDay(currentDate, this.localizer)));
  };

  onNavigate = (date: Date) => {
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
          eventStart: event.contractStart,
          eventEnd: event.contractEnd
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
            <Calendar
              events={eventList}
              views={['month', 'week', 'agenda']}
              localizer={this.localizer}
              messages={{
                date: translate('overview.calendar.date'),
                time: translate('overview.calendar.time'),
                event: translate('overview.calendar.event'),
                allDay: translate('overview.calendar.allDay'),
                week: translate('overview.calendar.week'),
                work_week: translate('overview.calendar.work_week'),
                day: translate('overview.calendar.day'),
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

const mapStateToProps = ({ locale, event }: IRootState) => ({
  locale: locale.currentLocale,
  eventList: event.entities.map(c => ({
    id: c.id,
    title: c.title,
    start: c.contractEnd,
    end: c.contractEnd,
    allDay: true,
    contractStart: c.contractStart,
    contractEnd: c.contractEnd
  }))
});

const mapDispatchToProps = { getSession, getEntities };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Overview);
