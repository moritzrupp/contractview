import { IPayload } from 'react-jhipster';
import { Moment } from 'moment';

export declare type ICrudGetAllBetweenAction<T> = (startDate: Moment, endDate: Moment, sort?: string) => IPayload<T> | ((dispatch: any) => IPayload<T>);

