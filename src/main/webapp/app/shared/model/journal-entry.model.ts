import { Moment } from 'moment';

export interface IJournalEntry {
  id?: number;
  date?: Moment;
  title?: string;
  description?: string;
}

export class JournalEntry implements IJournalEntry {
  constructor(public id?: number, public date?: Moment, public title?: string, public description?: string) {}
}
