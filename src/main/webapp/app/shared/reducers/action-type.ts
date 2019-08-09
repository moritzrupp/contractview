import { IPayload } from 'react-jhipster';
import { Moment } from 'moment';

export declare type IGetEvents<T> = (from: Moment, to: Moment) => IPayload<T> | ((dispatch: any) => IPayload<T>);
