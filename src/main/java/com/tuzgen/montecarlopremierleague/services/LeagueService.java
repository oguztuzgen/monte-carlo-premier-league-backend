package com.tuzgen.montecarlopremierleague.services;

import com.tuzgen.montecarlopremierleague.models.League;
import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.models.Week;

import java.util.List;

public interface LeagueService {
    List<Week> generateSchedule(List<Team> teams) throws CloneNotSupportedException;

    League createWithTeams(List<Team> teams);

    void simulateCurrentWeek(League league);

    void simulateCurrentWeekByLeagueId(Long id);

    void simulateAll(League league);
    void simulateAllByLeagueId(Long id);
}
