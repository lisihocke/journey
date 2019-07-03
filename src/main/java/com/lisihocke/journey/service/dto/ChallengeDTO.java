package com.lisihocke.journey.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lisihocke.journey.domain.Challenge} entity.
 */
public class ChallengeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String tag;

    @NotNull
    @Size(max = 3000)
    private String challengeDescription;

    @NotNull
    @Size(max = 1000)
    private String hypothesis;

    @NotNull
    @Size(max = 5000)
    private String probe;

    @Size(max = 3000)
    private String pauseCriteria;

    @NotNull
    @Size(max = 3000)
    private String exitCriteria;

    @Size(max = 8000)
    private String influences;

    @Size(max = 8000)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public void setChallengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getPauseCriteria() {
        return pauseCriteria;
    }

    public void setPauseCriteria(String pauseCriteria) {
        this.pauseCriteria = pauseCriteria;
    }

    public String getExitCriteria() {
        return exitCriteria;
    }

    public void setExitCriteria(String exitCriteria) {
        this.exitCriteria = exitCriteria;
    }

    public String getInfluences() {
        return influences;
    }

    public void setInfluences(String influences) {
        this.influences = influences;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChallengeDTO challengeDTO = (ChallengeDTO) o;
        if (challengeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), challengeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChallengeDTO{" +
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
