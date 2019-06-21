package com.lisihocke.journey.web.rest;

import com.lisihocke.journey.service.JournalEntryService;
import com.lisihocke.journey.web.rest.errors.BadRequestAlertException;
import com.lisihocke.journey.service.dto.JournalEntryDTO;
import com.lisihocke.journey.service.dto.JournalEntryCriteria;
import com.lisihocke.journey.service.JournalEntryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.lisihocke.journey.domain.JournalEntry}.
 */
@RestController
@RequestMapping("/api")
public class JournalEntryResource {

    private final Logger log = LoggerFactory.getLogger(JournalEntryResource.class);

    private static final String ENTITY_NAME = "journalEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JournalEntryService journalEntryService;

    private final JournalEntryQueryService journalEntryQueryService;

    public JournalEntryResource(JournalEntryService journalEntryService, JournalEntryQueryService journalEntryQueryService) {
        this.journalEntryService = journalEntryService;
        this.journalEntryQueryService = journalEntryQueryService;
    }

    /**
     * {@code POST  /journal-entries} : Create a new journalEntry.
     *
     * @param journalEntryDTO the journalEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new journalEntryDTO, or with status {@code 400 (Bad Request)} if the journalEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/journal-entries")
    public ResponseEntity<JournalEntryDTO> createJournalEntry(@Valid @RequestBody JournalEntryDTO journalEntryDTO) throws URISyntaxException {
        log.debug("REST request to save JournalEntry : {}", journalEntryDTO);
        if (journalEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new journalEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JournalEntryDTO result = journalEntryService.save(journalEntryDTO);
        return ResponseEntity.created(new URI("/api/journal-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /journal-entries} : Updates an existing journalEntry.
     *
     * @param journalEntryDTO the journalEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journalEntryDTO,
     * or with status {@code 400 (Bad Request)} if the journalEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the journalEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/journal-entries")
    public ResponseEntity<JournalEntryDTO> updateJournalEntry(@Valid @RequestBody JournalEntryDTO journalEntryDTO) throws URISyntaxException {
        log.debug("REST request to update JournalEntry : {}", journalEntryDTO);
        if (journalEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JournalEntryDTO result = journalEntryService.save(journalEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, journalEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /journal-entries} : get all the journalEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of journalEntries in body.
     */
    @GetMapping("/journal-entries")
    public ResponseEntity<List<JournalEntryDTO>> getAllJournalEntries(JournalEntryCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get JournalEntries by criteria: {}", criteria);
        Page<JournalEntryDTO> page = journalEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /journal-entries/count} : count all the journalEntries.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/journal-entries/count")
    public ResponseEntity<Long> countJournalEntries(JournalEntryCriteria criteria) {
        log.debug("REST request to count JournalEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(journalEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /journal-entries/:id} : get the "id" journalEntry.
     *
     * @param id the id of the journalEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the journalEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/journal-entries/{id}")
    public ResponseEntity<JournalEntryDTO> getJournalEntry(@PathVariable Long id) {
        log.debug("REST request to get JournalEntry : {}", id);
        Optional<JournalEntryDTO> journalEntryDTO = journalEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(journalEntryDTO);
    }

    /**
     * {@code DELETE  /journal-entries/:id} : delete the "id" journalEntry.
     *
     * @param id the id of the journalEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/journal-entries/{id}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable Long id) {
        log.debug("REST request to delete JournalEntry : {}", id);
        journalEntryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
