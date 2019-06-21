package com.lisihocke.journey.service.impl;

import com.lisihocke.journey.service.JournalEntryService;
import com.lisihocke.journey.domain.JournalEntry;
import com.lisihocke.journey.repository.JournalEntryRepository;
import com.lisihocke.journey.service.dto.JournalEntryDTO;
import com.lisihocke.journey.service.mapper.JournalEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link JournalEntry}.
 */
@Service
@Transactional
public class JournalEntryServiceImpl implements JournalEntryService {

    private final Logger log = LoggerFactory.getLogger(JournalEntryServiceImpl.class);

    private final JournalEntryRepository journalEntryRepository;

    private final JournalEntryMapper journalEntryMapper;

    public JournalEntryServiceImpl(JournalEntryRepository journalEntryRepository, JournalEntryMapper journalEntryMapper) {
        this.journalEntryRepository = journalEntryRepository;
        this.journalEntryMapper = journalEntryMapper;
    }

    /**
     * Save a journalEntry.
     *
     * @param journalEntryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public JournalEntryDTO save(JournalEntryDTO journalEntryDTO) {
        log.debug("Request to save JournalEntry : {}", journalEntryDTO);
        JournalEntry journalEntry = journalEntryMapper.toEntity(journalEntryDTO);
        journalEntry = journalEntryRepository.save(journalEntry);
        return journalEntryMapper.toDto(journalEntry);
    }

    /**
     * Get all the journalEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JournalEntries");
        return journalEntryRepository.findAll(pageable)
            .map(journalEntryMapper::toDto);
    }


    /**
     * Get one journalEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<JournalEntryDTO> findOne(Long id) {
        log.debug("Request to get JournalEntry : {}", id);
        return journalEntryRepository.findById(id)
            .map(journalEntryMapper::toDto);
    }

    /**
     * Delete the journalEntry by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JournalEntry : {}", id);
        journalEntryRepository.deleteById(id);
    }
}
