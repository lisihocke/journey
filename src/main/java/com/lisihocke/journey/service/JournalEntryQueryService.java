package com.lisihocke.journey.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.lisihocke.journey.domain.JournalEntry;
import com.lisihocke.journey.domain.*; // for static metamodels
import com.lisihocke.journey.repository.JournalEntryRepository;
import com.lisihocke.journey.service.dto.JournalEntryCriteria;
import com.lisihocke.journey.service.dto.JournalEntryDTO;
import com.lisihocke.journey.service.mapper.JournalEntryMapper;

/**
 * Service for executing complex queries for {@link JournalEntry} entities in the database.
 * The main input is a {@link JournalEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JournalEntryDTO} or a {@link Page} of {@link JournalEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JournalEntryQueryService extends QueryService<JournalEntry> {

    private final Logger log = LoggerFactory.getLogger(JournalEntryQueryService.class);

    private final JournalEntryRepository journalEntryRepository;

    private final JournalEntryMapper journalEntryMapper;

    public JournalEntryQueryService(JournalEntryRepository journalEntryRepository, JournalEntryMapper journalEntryMapper) {
        this.journalEntryRepository = journalEntryRepository;
        this.journalEntryMapper = journalEntryMapper;
    }

    /**
     * Return a {@link List} of {@link JournalEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JournalEntryDTO> findByCriteria(JournalEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JournalEntry> specification = createSpecification(criteria);
        return journalEntryMapper.toDto(journalEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JournalEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JournalEntryDTO> findByCriteria(JournalEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JournalEntry> specification = createSpecification(criteria);
        return journalEntryRepository.findAll(specification, page)
            .map(journalEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JournalEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JournalEntry> specification = createSpecification(criteria);
        return journalEntryRepository.count(specification);
    }

    /**
     * Function to convert JournalEntryCriteria to a {@link Specification}.
     */
    private Specification<JournalEntry> createSpecification(JournalEntryCriteria criteria) {
        Specification<JournalEntry> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JournalEntry_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), JournalEntry_.date));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), JournalEntry_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), JournalEntry_.description));
            }
        }
        return specification;
    }
}
