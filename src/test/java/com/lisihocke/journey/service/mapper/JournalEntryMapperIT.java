package com.lisihocke.journey.service.mapper;

import com.lisihocke.journey.JourneyApp;
import com.lisihocke.journey.domain.JournalEntry;
import com.lisihocke.journey.service.dto.JournalEntryDTO;
import com.lisihocke.journey.web.rest.ChallengeResourceIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.time.LocalDate;

/**
 * Integration tests for {@link JournalEntryMapper}.
 */
@SpringBootTest(classes = JourneyApp.class)
public class JournalEntryMapperIT {

    private static final Long DEFAULT_ID = 1L;

    @Autowired
    private JournalEntryMapper journalEntryMapper;

    @Autowired
    private EntityManager em;

    private JournalEntry journalEntry;
    private JournalEntryDTO journalEntryDto;

    @BeforeEach
    public void init() {
        journalEntry = new JournalEntry();
        journalEntry.setDate(LocalDate.ofEpochDay(0L));
        journalEntry.setTitle("title");
        journalEntry.setDescription("description");
        journalEntry.setChallenge(ChallengeResourceIT.createEntity(em));

        journalEntryDto = new JournalEntryDTO();
    }

    @Test
    public void testJournalEntryFromId() {
        Assertions.assertThat(journalEntryMapper.fromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        Assertions.assertThat(journalEntryMapper.fromId(null)).isNull();
    }
}
