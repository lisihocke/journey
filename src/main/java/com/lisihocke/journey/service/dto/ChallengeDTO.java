package com.lisihocke.journey.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.lisihocke.journey.domain.Challenge} entity.
 */
@Getter
@Setter
@ToString
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
}
