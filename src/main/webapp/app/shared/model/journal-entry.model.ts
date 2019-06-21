export interface IJournalEntry {
  id?: number;
  title?: string;
  description?: string;
}

export class JournalEntry implements IJournalEntry {
  constructor(public id?: number, public title?: string, public description?: string) {}
}
