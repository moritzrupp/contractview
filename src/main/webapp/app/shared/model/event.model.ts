import { Moment } from 'moment';
import { Event } from 'react-big-calendar';

export interface IEvent extends Event {
  id?: number;
  title?: string;
  contractStart?: Moment;
  contractEnd?: Moment;
  provider?: string;
  allDay?: boolean;
}

export const defaultValue: Readonly<IEvent> = {};
