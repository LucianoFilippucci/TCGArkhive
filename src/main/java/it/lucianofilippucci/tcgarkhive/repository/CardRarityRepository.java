package it.lucianofilippucci.tcgarkhive.repository;

import it.lucianofilippucci.tcgarkhive.entity.CardRarityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRarityRepository extends JpaRepository<CardRarityEntity, Long> {
}
