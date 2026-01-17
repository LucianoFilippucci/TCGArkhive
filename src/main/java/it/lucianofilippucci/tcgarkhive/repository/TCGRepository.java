package it.lucianofilippucci.tcgarkhive.repository;

import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TCGRepository extends JpaRepository<TCGEntity, Long> {
    Optional<TCGEntity> findByCode(String code);
}
