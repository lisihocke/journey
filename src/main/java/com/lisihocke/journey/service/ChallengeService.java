package com.lisihocke.journey.service;

import com.lisihocke.journey.service.dto.ChallengeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.lisihocke.journey.domain.Challenge}.
 */
public interface ChallengeService {

    /**
     * Save a challenge.
     *
     * @param challengeDTO the entity to save.
     * @return the persisted entity.
     */
    ChallengeDTO save(ChallengeDTO challengeDTO);

    /**
     * Get all the challenges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChallengeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" challenge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChallengeDTO> findOne(Long id);

    /**
     * Delete the "id" challenge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
