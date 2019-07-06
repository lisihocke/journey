package com.lisihocke.journey.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.lisihocke.journey.domain.Challenge} entity. This class is used
 * in {@link com.lisihocke.journey.web.rest.ChallengeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /challenges?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChallengeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tag;

    private LongFilter journalEntryId;

    public ChallengeCriteria(){
    }

    public ChallengeCriteria(ChallengeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.tag = other.tag == null ? null : other.tag.copy();
        this.journalEntryId = other.journalEntryId == null ? null : other.journalEntryId.copy();
    }

    @Override
    public ChallengeCriteria copy() {
        return new ChallengeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTag() {
        return tag;
    }

    public void setTag(StringFilter tag) {
        this.tag = tag;
    }

    public LongFilter getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(LongFilter journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChallengeCriteria that = (ChallengeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tag, that.tag) &&
            Objects.equals(journalEntryId, that.journalEntryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tag,
            journalEntryId
        );
    }

    @Override
    public String toString() {
        return "JournalEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tag != null ? "tag=" + tag + ", " : "") +
            (journalEntryId != null ? "journalEntryId=" + journalEntryId + ", " : "") +
            "}";
    }

}
