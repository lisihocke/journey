package com.lisihocke.journey.service.mapper;

import com.lisihocke.journey.JourneyApp;
import com.lisihocke.journey.domain.Challenge;
import com.lisihocke.journey.service.dto.ChallengeDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

/**
 * Integration tests for {@link ChallengeMapper}.
 */
@SpringBootTest(classes = JourneyApp.class)
public class ChallengeMapperIT {

    private static final Long DEFAULT_ID = 1L;

    @Autowired
    private ChallengeMapper challengeMapper;

    @Autowired
    private EntityManager em;

    private Challenge challenge;
    private ChallengeDTO challengeDto;

    @BeforeEach
    public void init() {
        challenge = new Challenge();
        challenge.setTag("tag");
        challenge.setChallengeDescription("challenge description");
        challenge.setHypothesis("hypothesis");
        challenge.setProbe("probe");
        challenge.setExitCriteria("exit criteria");

        challengeDto = new ChallengeDTO();
    }

    @Test
    public void testChallengeFromId() {
        Assertions.assertThat(challengeMapper.fromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        Assertions.assertThat(challengeMapper.fromId(null)).isNull();
    }
}
