package com.lisihocke.journey.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.lisihocke.journey.domain.JournalEntry} entity.
 */
@Getter
@Setter
@ToString
public class JournalEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 8000)
    private String description;

    @NotNull
    private Long challengeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JournalEntryDTO journalEntryDTO = (JournalEntryDTO) o;
        if (journalEntryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), journalEntryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
