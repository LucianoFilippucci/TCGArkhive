package it.lucianofilippucci.tcgarkhive.repository;

import it.lucianofilippucci.tcgarkhive.entity.TCGListEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TCGListEntryRepository extends JpaRepository<TCGListEntry, Long> {
}
