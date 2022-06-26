package com.tuzgen.montecarlopremierleague.repositories;

import com.tuzgen.montecarlopremierleague.models.Week;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekRepository extends JpaRepository<Week, Long> {
}
