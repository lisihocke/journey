package com.lisihocke.journey.web.rest;

import com.lisihocke.journey.service.ChallengeQueryService;
import com.lisihocke.journey.service.ChallengeService;
import com.lisihocke.journey.service.dto.ChallengeCriteria;
import com.lisihocke.journey.service.dto.ChallengeDTO;
import com.lisihocke.journey.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.lisihocke.journey.domain.Challenge}.
 */
@RestController
@RequestMapping("/api")
public class ChallengeResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeResource.class);

    private static final String ENTITY_NAME = "challenge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChallengeService challengeService;

    private final ChallengeQueryService challengeQueryService;

    public ChallengeResource(ChallengeService challengeService, ChallengeQueryService challengeQueryService) {
        this.challengeService = challengeService;
        this.challengeQueryService = challengeQueryService;
    }

    /**
     * {@code POST  /challenges} : Create a new challenge.
     *
     * @param challengeDTO the challengeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new challengeDTO, or with status {@code 400 (Bad Request)} if the challenge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/challenges")
    public ResponseEntity<ChallengeDTO> createChallenge(@Valid @RequestBody ChallengeDTO challengeDTO) throws URISyntaxException {
        log.debug("REST request to save Challenge : {}", challengeDTO);
        if (challengeDTO.getId() != null) {
            throw new BadRequestAlertException("A new challenge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChallengeDTO result = challengeService.save(challengeDTO);
        return ResponseEntity.created(new URI("/api/challenges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /challenges} : Updates an existing challenge.
     *
     * @param challengeDTO the challengeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated challengeDTO,
     * or with status {@code 400 (Bad Request)} if the challengeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the challengeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/challenges")
    public ResponseEntity<ChallengeDTO> updateChallenge(@Valid @RequestBody ChallengeDTO challengeDTO) throws URISyntaxException {
        log.debug("REST request to update JournalEntry : {}", challengeDTO);
        if (challengeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChallengeDTO result = challengeService.save(challengeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, challengeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /challenges} : get all the challenges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of challenges in body.
     */
    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges(ChallengeCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Challenges by criteria: {}", criteria);
        Page<ChallengeDTO> page = challengeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /challenges/count} : count all the challenges.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/challenges/count")
    public ResponseEntity<Long> countChallenges(ChallengeCriteria criteria) {
        log.debug("REST request to count Challenges by criteria: {}", criteria);
        return ResponseEntity.ok().body(challengeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /challenges/:id} : get the "id" challenge.
     *
     * @param id the id of the challengeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the challengeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/challenges/{id}")
    public ResponseEntity<ChallengeDTO> getChallenge(@PathVariable Long id) {
        log.debug("REST request to get Challenge : {}", id);
        Optional<ChallengeDTO> challengeDTO = challengeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(challengeDTO);
    }

    /**
     * {@code DELETE  /challenges/:id} : delete the "id" challenge.
     *
     * @param id the id of the challengeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        log.debug("REST request to delete Challenge : {}", id);
        challengeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
