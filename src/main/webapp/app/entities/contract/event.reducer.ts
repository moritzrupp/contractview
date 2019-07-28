import axios from 'axios';
import { Moment } from 'moment';
import { log } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IEvent } from 'app/shared/model/event.model';
import { IGetEvents } from 'app/shared/reducers/action-type';
import { defaultValue } from 'app/shared/model/provider.model';

export const ACTION_TYPES = {
  FETCH_EVENT_LIST: 'event/FETCH_EVENT_LIST',
  RESET: 'event/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as IEvent[],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EventState = Readonly<typeof initialState>;

// Reducer

export default (state: EventState = initialState, action): EventState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EVENT_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EVENT_LIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EVENT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/events';

// Actions

export const getEntities: IGetEvents<IEvent> = (year?: string, month?: string, from?: Moment, to?: Moment) => {
  const params = `${
    from && to
      ? `?from=${from.toISOString()}&to=${to.toISOString()}`
      : from && !to
        ? `?from=${from.toISOString()}`
        : !from && to
          ? `?to=${to.toISOString()}`
          : ''
  }`;
  log('params:', params);
  const requestUrl = `${apiUrl}${year ? `/${year}` : ''}${year && month ? `/${month}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EVENT_LIST,
    payload: axios.get<IEvent>(`${requestUrl}${params ? `${params}&` : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
