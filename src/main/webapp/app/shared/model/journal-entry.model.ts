import { Moment } from 'moment';
import { Challenge } from './challenge.model';

export interface IJournalEntry {
  id?: number;
  date?: Moment;
  title?: string;
  description?: string;
  challengeId?: number;
}

export class JournalEntry implements IJournalEntry {
  constructor(public id?: number, public date?: Moment, public title?: string, public description?: string, public challengeId?: number) {}
}
