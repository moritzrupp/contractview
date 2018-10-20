export interface IProvider {
  id?: number;
  name?: string;
  website?: string;
}

export const defaultValue: Readonly<IProvider> = {};
