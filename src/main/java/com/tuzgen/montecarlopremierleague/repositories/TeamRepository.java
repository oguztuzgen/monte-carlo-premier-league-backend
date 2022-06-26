package com.tuzgen.montecarlopremierleague.repositories;

import com.tuzgen.montecarlopremierleague.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
