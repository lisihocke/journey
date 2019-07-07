package com.lisihocke.journey.web.rest;

import com.lisihocke.journey.JourneyApp;
import com.lisihocke.journey.domain.Challenge;
import com.lisihocke.journey.domain.JournalEntry;
import com.lisihocke.journey.repository.ChallengeRepository;
import com.lisihocke.journey.service.ChallengeQueryService;
import com.lisihocke.journey.service.ChallengeService;
import com.lisihocke.journey.service.dto.ChallengeDTO;
import com.lisihocke.journey.service.mapper.ChallengeMapper;
import com.lisihocke.journey.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.lisihocke.journey.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@Link ChallengeResource} REST controller.
 */
@SpringBootTest(classes = JourneyApp.class)
public class ChallengeResourceIT {

    private static final String DEFAULT_TAG = "defaultTag";
    private static final String UPDATED_TAG = "updatedTag";

    private static final String DEFAULT_CHALLENGE_DESCRIPTION = "defaultChallengeDescription";
    private static final String UPDATED_CHALLENGE_DESCRIPTION = "updatedChallengeDescription";

    private static final String DEFAULT_HYPOTHESIS = "defaultHypothesis";
    private static final String UPDATED_HYPOTHESIS = "updatedHypothesis";

    private static final String DEFAULT_PROBE = "defaultProbe";
    private static final String UPDATED_PROBE = "updatedProbe";

    private static final String DEFAULT_EXIT_CRITERIA = "defaultExitCriteria";
    private static final String UPDATED_EXIT_CRITERIA = "updatedExitCriteria";

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeMapper challengeMapper;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeQueryService challengeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restChallengeMockMvc;

    private Challenge challenge;
/*
    private static JournalEntry defaultJournalEntry;

    private static JournalEntry updatedJournalEntry;*/

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChallengeResource challengeResource = new ChallengeResource(challengeService, challengeQueryService);
        this.restChallengeMockMvc = MockMvcBuilders.standaloneSetup(challengeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Challenge createEntity(EntityManager em) {
        Challenge challenge = new Challenge()
            .tag(DEFAULT_TAG)
            .challengeDescription(DEFAULT_CHALLENGE_DESCRIPTION)
            .hypothesis(DEFAULT_HYPOTHESIS)
            .probe(DEFAULT_PROBE)
            .exitCriteria(DEFAULT_EXIT_CRITERIA);

        return challenge;
    }

    @BeforeEach
    public void initTest() {
        /*defaultJournalEntry = JournalEntryResourceIT.createEntity(em);
        em.persist(defaultJournalEntry);

        updatedJournalEntry = JournalEntryResourceIT.createEntity(em);
        em.persist(updatedJournalEntry);
        em.flush();*/

        challenge = createEntity(em);
        //challenge.addJournalEntry(defaultJournalEntry);
    }

    @Test
    @Transactional
    public void createChallenge() throws Exception {
        int databaseSizeBeforeCreate = challengeRepository.findAll().size();

        // Create the Challenge
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);
        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isCreated());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeCreate + 1);
        Challenge testChallenge = challengeList.get(challengeList.size() - 1);
        assertThat(testChallenge.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testChallenge.getChallengeDescription()).isEqualTo(DEFAULT_CHALLENGE_DESCRIPTION);
        assertThat(testChallenge.getHypothesis()).isEqualTo(DEFAULT_HYPOTHESIS);
        assertThat(testChallenge.getProbe()).isEqualTo(DEFAULT_PROBE);
        assertThat(testChallenge.getExitCriteria()).isEqualTo(DEFAULT_EXIT_CRITERIA);
    }

    @Test
    @Transactional
    public void createChallengeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = challengeRepository.findAll().size();

        // Create the Challenge with an existing ID
        challenge.setId(1L);
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setTag(null);

        // Create the Challenge, which fails.
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChallengeDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setChallengeDescription(null);

        // Create the Challenge, which fails.
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHypothesisIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setHypothesis(null);

        // Create the Challenge, which fails.
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProbeIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setProbe(null);

        // Create the Challenge, which fails.
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExitCriteriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = challengeRepository.findAll().size();
        // set the field null
        challenge.setExitCriteria(null);

        // Create the Challenge, which fails.
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        restChallengeMockMvc.perform(post("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChallenges() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList
        restChallengeMockMvc.perform(get("/api/challenges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(challenge.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].challengeDescription").value(hasItem(DEFAULT_CHALLENGE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hypothesis").value(hasItem(DEFAULT_HYPOTHESIS)))
            .andExpect(jsonPath("$.[*].probe").value(hasItem(DEFAULT_PROBE)))
            .andExpect(jsonPath("$.[*].exitCriteria").value(hasItem(DEFAULT_EXIT_CRITERIA)));
    }
    
    @Test
    @Transactional
    public void getChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get the challenge
        restChallengeMockMvc.perform(get("/api/challenges/{id}", challenge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(challenge.getId().intValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.challengeDescription").value(DEFAULT_CHALLENGE_DESCRIPTION))
            .andExpect(jsonPath("$.hypothesis").value(DEFAULT_HYPOTHESIS))
            .andExpect(jsonPath("$.probe").value(DEFAULT_PROBE))
            .andExpect(jsonPath("$.exitCriteria").value(DEFAULT_EXIT_CRITERIA));
    }

    @Test
    @Transactional
    public void getAllChallengesByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where tag equals to DEFAULT_TAG
        defaultChallengeShouldBeFound("tag.equals=" + DEFAULT_TAG);

        // Get all the challengeList where tag equals to UPDATED_TAG
        defaultChallengeShouldNotBeFound("tag.equals=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    public void getAllChallengesByTagIsInShouldWork() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where tag in DEFAULT_TAG or UPDATED_TAG
        defaultChallengeShouldBeFound("tag.in=" + DEFAULT_TAG + "," + UPDATED_TAG);

        // Get all the challengeList where tag equals to UPDATED_TAG
        defaultChallengeShouldNotBeFound("tag.in=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    public void getAllChallengesByTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        // Get all the challengeList where tag is not null
        defaultChallengeShouldBeFound("tag.specified=true");

        // Get all the challengeList where tag is null
        defaultChallengeShouldNotBeFound("tag.specified=false");
    }

    @Test
    @Transactional
    public void getAllChallengesByJournalEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        JournalEntry journalEntry= JournalEntryResourceIT.createEntity(em);
        em.persist(journalEntry);
        em.flush();
        challenge.addJournalEntry(journalEntry);
        challengeRepository.saveAndFlush(challenge);
        Long journalEntryId = journalEntry.getId();

        // Get all the challengeList where journalEntryId equals to defaultJournalEntry
        defaultChallengeShouldBeFound("journalEntryId.equals=" + journalEntryId);

        // Get all the challengeList where journalEntryId equals to updatedJournalEntry
        defaultChallengeShouldNotBeFound("journalEntryId.equals=" + (journalEntryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChallengeShouldBeFound(String filter) throws Exception {
        restChallengeMockMvc.perform(get("/api/challenges?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(challenge.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].challengeDescription").value(hasItem(DEFAULT_CHALLENGE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hypothesis").value(hasItem(DEFAULT_HYPOTHESIS)))
            .andExpect(jsonPath("$.[*].probe").value(hasItem(DEFAULT_PROBE)))
            .andExpect(jsonPath("$.[*].exitCriteria").value(hasItem(DEFAULT_EXIT_CRITERIA)));

        // Check, that the count call also returns 1
        restChallengeMockMvc.perform(get("/api/challenges/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChallengeShouldNotBeFound(String filter) throws Exception {
        restChallengeMockMvc.perform(get("/api/challenges?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChallengeMockMvc.perform(get("/api/challenges/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingChallenge() throws Exception {
        // Get the challenge
        restChallengeMockMvc.perform(get("/api/challenges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();

        // Update the challenge
        Challenge updatedChallenge = challengeRepository.findById(challenge.getId()).get();
        // Disconnect from session so that the updates on updatedChallenge are not directly saved in db
        em.detach(updatedChallenge);
        updatedChallenge
            .tag(UPDATED_TAG)
            .challengeDescription(UPDATED_CHALLENGE_DESCRIPTION)
            .hypothesis(UPDATED_HYPOTHESIS)
            .probe((UPDATED_PROBE))
            .exitCriteria(UPDATED_EXIT_CRITERIA);
        ChallengeDTO challengeDTO = challengeMapper.toDto(updatedChallenge);

        restChallengeMockMvc.perform(put("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isOk());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
        Challenge testChallenge = challengeList.get(challengeList.size() - 1);
        assertThat(testChallenge.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testChallenge.getChallengeDescription()).isEqualTo(UPDATED_CHALLENGE_DESCRIPTION);
        assertThat(testChallenge.getHypothesis()).isEqualTo(UPDATED_HYPOTHESIS);
        assertThat(testChallenge.getProbe()).isEqualTo(UPDATED_PROBE);
        assertThat(testChallenge.getExitCriteria()).isEqualTo(UPDATED_EXIT_CRITERIA);
    }

    @Test
    @Transactional
    public void updateNonExistingChallenge() throws Exception {
        int databaseSizeBeforeUpdate = challengeRepository.findAll().size();

        // Create the Challenge
        ChallengeDTO challengeDTO = challengeMapper.toDto(challenge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChallengeMockMvc.perform(put("/api/challenges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(challengeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Challenge in the database
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChallenge() throws Exception {
        // Initialize the database
        challengeRepository.saveAndFlush(challenge);

        int databaseSizeBeforeDelete = challengeRepository.findAll().size();

        // Delete the challenge
        restChallengeMockMvc.perform(delete("/api/challenges/{id}", challenge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Challenge> challengeList = challengeRepository.findAll();
        assertThat(challengeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Challenge.class);
        Challenge challenge1 = new Challenge();
        challenge1.setId(1L);
        Challenge challenge = new Challenge();
        challenge.setId(challenge1.getId());
        assertThat(challenge1).isEqualTo(challenge);
        challenge.setId(2L);
        assertThat(challenge1).isNotEqualTo(challenge);
        challenge1.setId(null);
        assertThat(challenge1).isNotEqualTo(challenge);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChallengeDTO.class);
        ChallengeDTO challengeDTO1 = new ChallengeDTO();
        challengeDTO1.setId(1L);
        ChallengeDTO challengeDTO2 = new ChallengeDTO();
        assertThat(challengeDTO1).isNotEqualTo(challengeDTO2);
        challengeDTO2.setId(challengeDTO1.getId());
        assertThat(challengeDTO1).isEqualTo(challengeDTO2);
        challengeDTO2.setId(2L);
        assertThat(challengeDTO1).isNotEqualTo(challengeDTO2);
        challengeDTO1.setId(null);
        assertThat(challengeDTO1).isNotEqualTo(challengeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(challengeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(challengeMapper.fromId(null)).isNull();
    }
}
