import { IJournalEntry } from './journal-entry.model';

export interface IChallenge {
  id?: number;
  tag?: string;
  challengeDescription?: string;
  hypothesis?: string;
  probe?: string;
  pauseCriteria?: string;
  exitCriteria?: string;
  influences?: string;
  notes?: string;
  journalEntries?: IJournalEntry[];
}

export class Challenge implements IChallenge {
  constructor(
    public id?: number,
    public tag?: string,
    public challengeDescription?: string,
    public hypothesis?: string,
    public probe?: string,
    public pauseCriteria?: string,
    public exitCriteria?: string,
    public influences?: string,
    public notes?: string,
    public journalEntries?: IJournalEntry[]
  ) {}
}
