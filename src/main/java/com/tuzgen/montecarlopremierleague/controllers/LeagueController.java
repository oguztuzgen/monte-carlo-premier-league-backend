package com.tuzgen.montecarlopremierleague.controllers;

import com.tuzgen.montecarlopremierleague.models.League;
import com.tuzgen.montecarlopremierleague.repositories.LeagueRepository;
import com.tuzgen.montecarlopremierleague.repositories.TeamRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/league")
@RestController
public class LeagueController {
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    public LeagueController(LeagueRepository leagueRepository, TeamRepository teamRepository) {
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/all")
    public List<League> displayAllLeagues() {
        return leagueRepository.findAll();
    }

    @GetMapping("/{leagueId}")
    public League displayLeague(@PathVariable Long leagueId) {
        return leagueRepository.findById(leagueId).orElseThrow(RuntimeException::new);
    }

//    @PostMapping("")
//    public League createLeague(@RequestBody League league) {
//        teamRepository.saveAll(league.getTeams());
//        return leagueRepository.save(league);
//    }

}
