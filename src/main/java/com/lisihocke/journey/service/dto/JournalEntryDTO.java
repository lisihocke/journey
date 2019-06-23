package com.lisihocke.journey.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.lisihocke.journey.domain.JournalEntry} entity.
 */
public class JournalEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 8000)
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    @Override
    public String toString() {
        return "JournalEntryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
