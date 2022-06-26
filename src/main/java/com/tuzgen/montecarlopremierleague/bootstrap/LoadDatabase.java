package com.tuzgen.montecarlopremierleague.bootstrap;

import com.tuzgen.montecarlopremierleague.models.League;
import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.repositories.MatchRepository;
import com.tuzgen.montecarlopremierleague.repositories.TeamRepository;
import com.tuzgen.montecarlopremierleague.services.LeagueService;
import com.tuzgen.montecarlopremierleague.services.MatchService;
import com.tuzgen.montecarlopremierleague.utils.CsvParser;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
@Profile("dev")
public class LoadDatabase {
    private final TeamRepository teamRepository;

    private final LeagueService leagueService;
    private final MatchService matchService;

    public LoadDatabase(TeamRepository teamRepository, MatchRepository matchRepository, LeagueService leagueService, MatchService matchService) {
        this.teamRepository = teamRepository;
        this.leagueService = leagueService;
        this.matchService = matchService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        List<Team> teams = CsvParser.createTeamsFromResourceCsv();

        teamRepository.saveAll(teams);

        League league = leagueService.createWithTeams(teams);

//        leagueService.monteCarloSimulation(league.getId(), 2);

        System.out.println("Finished database bootstrap");


    }
}
