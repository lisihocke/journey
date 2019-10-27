package com.lisihocke.journey.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Challenge.
 */
@Entity
@Table(name = "challenge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@ToString
public class Challenge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "tag", length = 100, unique = true)
    private String tag;

    @NotNull
    @Lob
    @Column(name = "challenge_description")
    private String challengeDescription;

    @NotNull
    @Lob
    @Column(name = "hypothesis")
    private String hypothesis;

    @NotNull
    @Lob
    @Column(name = "probe")
    private String probe;

    @Lob
    @Column(name = "pause_criteria")
    private String pauseCriteria;

    @NotNull
    @Lob
    @Column(name = "exit_criteria")
    private String exitCriteria;

    @Lob
    @Column(name = "influences")
    private String influences;

    @Lob
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "challenge")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JournalEntry> journalEntries = new HashSet<>();

    public Challenge tag(String tag) {
        this.tag = tag;
        return this;
    }

    public Challenge challengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
        return this;
    }

    public Challenge hypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
        return this;
    }

    public Challenge probe(String probe) {
        this.probe = probe;
        return this;
    }

    public Challenge pauseCriteria(String pauseCriteria) {
        this.pauseCriteria = pauseCriteria;
        return this;
    }

    public Challenge exitCriteria(String exitCriteria) {
        this.exitCriteria = exitCriteria;
        return this;
    }

    public Challenge influences(String influences) {
        this.influences = influences;
        return this;
    }

    public Challenge notes(String notes) {
        this.notes = notes;
        return this;
    }

    public Challenge journalEntries(Set<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
        return this;
    }

    public Challenge addJournalEntry(JournalEntry journalEntry) {
        this.journalEntries.add(journalEntry);
        journalEntry.setChallenge(this);
        return this;
    }

    public Challenge removeJournalEntry(JournalEntry journalEntry) {
        this.journalEntries.remove(journalEntry);
        journalEntry.setChallenge(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Challenge)) {
            return false;
        }
        return id != null && id.equals(((Challenge) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
