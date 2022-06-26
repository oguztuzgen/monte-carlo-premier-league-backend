package com.tuzgen.montecarlopremierleague.repositories;

import com.tuzgen.montecarlopremierleague.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
