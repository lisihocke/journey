package com.lisihocke.journey.repository;

import com.lisihocke.journey.domain.JournalEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the JournalEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long>, JpaSpecificationExecutor<JournalEntry> {

}
