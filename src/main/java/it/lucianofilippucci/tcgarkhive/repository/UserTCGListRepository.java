package it.lucianofilippucci.tcgarkhive.repository;

import it.lucianofilippucci.tcgarkhive.entity.UserTCGList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTCGListRepository extends JpaRepository<UserTCGList, Long> {


}
