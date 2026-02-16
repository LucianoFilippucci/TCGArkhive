package it.lucianofilippucci.tcgarkhive.repository;

import it.lucianofilippucci.tcgarkhive.entity.CardEntity;
import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByCardNameContainsAndTcgIs(String cardName, TCGEntity tcgEntity);

    Optional<CardEntity> findByIdIsAndTcgIs(Long id, TCGEntity tcgEntity);
}
