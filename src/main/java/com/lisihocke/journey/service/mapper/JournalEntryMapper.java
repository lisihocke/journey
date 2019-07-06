package com.lisihocke.journey.service.mapper;

import com.lisihocke.journey.domain.JournalEntry;
import com.lisihocke.journey.service.dto.JournalEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link JournalEntry} and its DTO {@link JournalEntryDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChallengeMapper.class})
public interface JournalEntryMapper extends EntityMapper<JournalEntryDTO, JournalEntry> {

    @Mapping(source = "challenge.id", target = "challengeId")
    JournalEntryDTO toDto(JournalEntry journalEntry);

    @Mapping(source = "challengeId", target = "challenge")
    JournalEntry toEntity(JournalEntryDTO journalEntryDTO);

    default JournalEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(id);
        return journalEntry;
    }
}
