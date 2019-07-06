package com.lisihocke.journey.service.mapper;

import com.lisihocke.journey.domain.Challenge;
import com.lisihocke.journey.service.dto.ChallengeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Challenge} and its DTO {@link ChallengeDTO}.
 */
@Mapper(componentModel = "spring", uses = {JournalEntryMapper.class})
public interface ChallengeMapper extends EntityMapper<ChallengeDTO, Challenge> {

    @Mapping(target = "journalEntries", ignore = true)
    @Mapping(target = "removeJournalEntry", ignore = true)
    Challenge toEntity(ChallengeDTO challengeDTO);

    default Challenge fromId(Long id) {
        if (id == null) {
            return null;
        }
        Challenge challenge = new Challenge();
        challenge.setId(id);
        return challenge;
    }
}
