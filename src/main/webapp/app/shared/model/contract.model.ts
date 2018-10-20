import { Moment } from 'moment';
import { IProvider } from 'app/shared/model//provider.model';
import { IUser } from 'app/shared/model/user.model';

export interface IContract {
  id?: number;
  name?: string;
  contactEmail?: string;
  price?: number;
  billingPeriodDays?: number;
  contractStart?: Moment;
  contractEnd?: Moment;
  automaticExtension?: boolean;
  extensionPeriodDays?: number;
  extensionReminder?: boolean;
  extensionReminderPeriodDays?: number;
  provider?: IProvider;
  owner?: IUser;
  users?: IUser[];
}

export const defaultValue: Readonly<IContract> = {
  automaticExtension: false,
  extensionReminder: false
};
