package com.lisihocke.journey.web.rest;

import com.lisihocke.journey.JourneyApp;
import com.lisihocke.journey.domain.Challenge;
import com.lisihocke.journey.domain.JournalEntry;
import com.lisihocke.journey.repository.JournalEntryRepository;
import com.lisihocke.journey.service.JournalEntryQueryService;
import com.lisihocke.journey.service.JournalEntryService;
import com.lisihocke.journey.service.dto.JournalEntryDTO;
import com.lisihocke.journey.service.mapper.JournalEntryMapper;
import com.lisihocke.journey.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@Link JournalEntryResource} REST controller.
 */
@SpringBootTest(classes = JourneyApp.class)
@AutoConfigureMockMvc
public class JournalEntryResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TITLE = "defaultTitle";
    private static final String UPDATED_TITLE = "updatedTitle";

    private static final String DEFAULT_DESCRIPTION = "defaultDescription";
    private static final String UPDATED_DESCRIPTION = "updatedDescription";

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalEntryMapper journalEntryMapper;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private JournalEntryQueryService journalEntryQueryService;

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

    private MockMvc restJournalEntryMockMvc;

    private JournalEntry journalEntry;

    private static Challenge defaultChallenge;

    private static Challenge updatedChallenge;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JournalEntryResource journalEntryResource = new JournalEntryResource(journalEntryService, journalEntryQueryService);
        this.restJournalEntryMockMvc = MockMvcBuilders.standaloneSetup(journalEntryResource)
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
    public static JournalEntry createEntity(EntityManager em) {
        defaultChallenge = ChallengeResourceIT.createEntity(em);
        em.persist(defaultChallenge);
        em.flush();

        JournalEntry journalEntry = new JournalEntry()
            .date(DEFAULT_DATE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);

        journalEntry.setChallenge(defaultChallenge);

        return journalEntry;
    }

    @BeforeEach
    public void initTest() {
        updatedChallenge = ChallengeResourceIT.createEntity(em);
        em.persist(updatedChallenge);
        em.flush();

        journalEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createJournalEntry() throws Exception {
        int databaseSizeBeforeCreate = journalEntryRepository.findAll().size();

        // Create the JournalEntry
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(journalEntry);
        restJournalEntryMockMvc.perform(post("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the JournalEntry in the database
        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeCreate + 1);
        JournalEntry testJournalEntry = journalEntryList.get(journalEntryList.size() - 1);
        assertThat(testJournalEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testJournalEntry.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJournalEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJournalEntry.getChallenge().getId()).isEqualTo(defaultChallenge.getId());
    }

    @Test
    @Transactional
    public void createJournalEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = journalEntryRepository.findAll().size();

        // Create the JournalEntry with an existing ID
        journalEntry.setId(1L);
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(journalEntry);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalEntryMockMvc.perform(post("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JournalEntry in the database
        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = journalEntryRepository.findAll().size();
        // set the field null
        journalEntry.setDate(null);

        // Create the JournalEntry, which fails.
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(journalEntry);

        restJournalEntryMockMvc.perform(post("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isBadRequest());

        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = journalEntryRepository.findAll().size();
        // set the field null
        journalEntry.setTitle(null);

        // Create the JournalEntry, which fails.
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(journalEntry);

        restJournalEntryMockMvc.perform(post("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isBadRequest());

        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChallengeIsRequired() throws Exception {
        int databaseSizeBeforeTest = journalEntryRepository.findAll().size();
        // set the field null
        journalEntry.setChallenge(null);

        // Create the JournalEntry, which fails.
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(journalEntry);

        restJournalEntryMockMvc.perform(post("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isBadRequest());

        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJournalEntries() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList
        restJournalEntryMockMvc.perform(get("/api/journal-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].challengeId").value(hasItem(defaultChallenge.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getJournalEntry() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get the journalEntry
        restJournalEntryMockMvc.perform(get("/api/journal-entries/{id}", journalEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(journalEntry.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.challengeId").value(defaultChallenge.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where date equals to DEFAULT_DATE
        defaultJournalEntryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the journalEntryList where date equals to UPDATED_DATE
        defaultJournalEntryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where date in DEFAULT_DATE or UPDATED_DATE
        defaultJournalEntryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the journalEntryList where date equals to UPDATED_DATE
        defaultJournalEntryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where date is not null
        defaultJournalEntryShouldBeFound("date.specified=true");

        // Get all the journalEntryList where date is null
        defaultJournalEntryShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where date greater than or equals to DEFAULT_DATE
        defaultJournalEntryShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the journalEntryList where date greater than or equals to UPDATED_DATE
        defaultJournalEntryShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where date less than or equals to DEFAULT_DATE
        defaultJournalEntryShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the journalEntryList where date less than or equals to UPDATED_DATE
        defaultJournalEntryShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where title equals to DEFAULT_TITLE
        defaultJournalEntryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the journalEntryList where title equals to UPDATED_TITLE
        defaultJournalEntryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultJournalEntryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the journalEntryList where title equals to UPDATED_TITLE
        defaultJournalEntryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where title is not null
        defaultJournalEntryShouldBeFound("title.specified=true");

        // Get all the journalEntryList where title is null
        defaultJournalEntryShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where description equals to DEFAULT_DESCRIPTION
        defaultJournalEntryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the journalEntryList where description equals to UPDATED_DESCRIPTION
        defaultJournalEntryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultJournalEntryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the journalEntryList where description equals to UPDATED_DESCRIPTION
        defaultJournalEntryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where description is not null
        defaultJournalEntryShouldBeFound("description.specified=true");

        // Get all the journalEntryList where description is null
        defaultJournalEntryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByChallengeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where challengeId equals to defaultChallenge
            defaultJournalEntryShouldBeFound("challengeId.equals=" + defaultChallenge.getId());

        // Get all the journalEntryList where challengeId equals to updatedChallenge
        defaultJournalEntryShouldNotBeFound("challengeId.equals=" + updatedChallenge.getId());
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByChallengeIdIsInShouldWork() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where challengeId in defaultChallenge or updatedChallenge
        defaultJournalEntryShouldBeFound("challengeId.in=" + defaultChallenge.getId() + "," + updatedChallenge.getId());

        // Get all the journalEntryList where challengeId equals to updatedChallenge
        defaultJournalEntryShouldNotBeFound("challengeId.in=" + updatedChallenge.getId());
    }

    @Test
    @Transactional
    public void getAllJournalEntriesByChallengeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        // Get all the journalEntryList where challengeId is not null
        defaultJournalEntryShouldBeFound("challengeId.specified=true");

        // Get all the journalEntryList where challengeId is null
        defaultJournalEntryShouldNotBeFound("challengeId.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJournalEntryShouldBeFound(String filter) throws Exception {
        restJournalEntryMockMvc.perform(get("/api/journal-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].challengeId").value(hasItem(defaultChallenge.getId().intValue())));

        // Check, that the count call also returns 1
        restJournalEntryMockMvc.perform(get("/api/journal-entries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJournalEntryShouldNotBeFound(String filter) throws Exception {
        restJournalEntryMockMvc.perform(get("/api/journal-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJournalEntryMockMvc.perform(get("/api/journal-entries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingJournalEntry() throws Exception {
        // Get the journalEntry
        restJournalEntryMockMvc.perform(get("/api/journal-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJournalEntry() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        int databaseSizeBeforeUpdate = journalEntryRepository.findAll().size();

        // Update the journalEntry
        JournalEntry updatedJournalEntry = journalEntryRepository.findById(journalEntry.getId()).get();
        // Disconnect from session so that the updates on updatedJournalEntry are not directly saved in db
        em.detach(updatedJournalEntry);
        updatedJournalEntry
            .date(UPDATED_DATE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .setChallenge(updatedChallenge);
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(updatedJournalEntry);

        restJournalEntryMockMvc.perform(put("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isOk());

        // Validate the JournalEntry in the database
        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeUpdate);
        JournalEntry testJournalEntry = journalEntryList.get(journalEntryList.size() - 1);
        assertThat(testJournalEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJournalEntry.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJournalEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJournalEntry.getChallenge()).isEqualTo(updatedChallenge);
    }

    @Test
    @Transactional
    public void updateNonExistingJournalEntry() throws Exception {
        int databaseSizeBeforeUpdate = journalEntryRepository.findAll().size();

        // Create the JournalEntry
        JournalEntryDTO journalEntryDTO = journalEntryMapper.toDto(journalEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalEntryMockMvc.perform(put("/api/journal-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journalEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JournalEntry in the database
        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJournalEntry() throws Exception {
        // Initialize the database
        journalEntryRepository.saveAndFlush(journalEntry);

        int databaseSizeBeforeDelete = journalEntryRepository.findAll().size();

        // Delete the journalEntry
        restJournalEntryMockMvc.perform(delete("/api/journal-entries/{id}", journalEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<JournalEntry> journalEntryList = journalEntryRepository.findAll();
        assertThat(journalEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalEntry.class);
        JournalEntry journalEntry1 = new JournalEntry();
        journalEntry1.setId(1L);
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(journalEntry1.getId());
        assertThat(journalEntry1).isEqualTo(journalEntry);
        journalEntry.setId(2L);
        assertThat(journalEntry1).isNotEqualTo(journalEntry);
        journalEntry1.setId(null);
        assertThat(journalEntry1).isNotEqualTo(journalEntry);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JournalEntryDTO.class);
        JournalEntryDTO journalEntryDTO1 = new JournalEntryDTO();
        journalEntryDTO1.setId(1L);
        JournalEntryDTO journalEntryDTO2 = new JournalEntryDTO();
        assertThat(journalEntryDTO1).isNotEqualTo(journalEntryDTO2);
        journalEntryDTO2.setId(journalEntryDTO1.getId());
        assertThat(journalEntryDTO1).isEqualTo(journalEntryDTO2);
        journalEntryDTO2.setId(2L);
        assertThat(journalEntryDTO1).isNotEqualTo(journalEntryDTO2);
        journalEntryDTO1.setId(null);
        assertThat(journalEntryDTO1).isNotEqualTo(journalEntryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(journalEntryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(journalEntryMapper.fromId(null)).isNull();
    }
}
