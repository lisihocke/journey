package com.lisihocke.journey.service;

import com.lisihocke.journey.service.dto.JournalEntryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.lisihocke.journey.domain.JournalEntry}.
 */
public interface JournalEntryService {

    /**
     * Save a journalEntry.
     *
     * @param journalEntryDTO the entity to save.
     * @return the persisted entity.
     */
    JournalEntryDTO save(JournalEntryDTO journalEntryDTO);

    /**
     * Get all the journalEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JournalEntryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" journalEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JournalEntryDTO> findOne(Long id);

    /**
     * Delete the "id" journalEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
