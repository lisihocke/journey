package com.lisihocke.journey.domain;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public Challenge tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public Challenge challengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
        return this;
    }

    public void setChallengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public Challenge hypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
        return this;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }

    public String getProbe() {
        return probe;
    }

    public Challenge probe(String probe) {
        this.probe = probe;
        return this;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getPauseCriteria() {
        return pauseCriteria;
    }

    public Challenge pauseCriteria(String pauseCriteria) {
        this.pauseCriteria = pauseCriteria;
        return this;
    }

    public void setPauseCriteria(String pauseCriteria) {
        this.pauseCriteria = pauseCriteria;
    }

    public String getExitCriteria() {
        return exitCriteria;
    }

    public Challenge exitCriteria(String exitCriteria) {
        this.exitCriteria = exitCriteria;
        return this;
    }

    public void setExitCriteria(String exitCriteria) {
        this.exitCriteria = exitCriteria;
    }

    public String getInfluences() {
        return influences;
    }

    public Challenge influences(String influences) {
        this.influences = influences;
        return this;
    }

    public void setInfluences(String influences) {
        this.influences = influences;
    }

    public String getNotes() {
        return notes;
    }

    public Challenge notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<JournalEntry> getJournalEntries() {
        return journalEntries;
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

    public void setJournalEntries(Set<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
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

    @Override
    public String toString() {
        return "Challenge{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            ", challengeDescription='" + getChallengeDescription() + "'" +
            ", hypothesis='" + getHypothesis() + "'" +
            ", probe='" + getProbe() + "'" +
            ", pauseCriteria='" + getPauseCriteria() + "'" +
            ", exitCriteria='" + getExitCriteria() + "'" +
            ", influences='" + getInfluences() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
