import { IPayload } from 'react-jhipster';
import { Moment } from 'moment';

export declare type IGetEvents<T> = (
  year?: string,
  month?: string,
  from?: Moment,
  to?: Moment
) => IPayload<T> | ((dispatch: any) => IPayload<T>);
