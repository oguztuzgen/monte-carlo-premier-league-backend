package com.tuzgen.montecarlopremierleague.repositories;

import com.tuzgen.montecarlopremierleague.models.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}
