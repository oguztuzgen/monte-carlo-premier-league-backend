package com.tuzgen.montecarlopremierleague.controllers;

import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.services.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/simulation")
@RestController
public class SimulationController {

    private final LeagueService leagueService;

    public SimulationController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @PostMapping("/single/{leagueId}")
    public ResponseEntity<String> simulateSingleWeekInLeague(@PathVariable Long leagueId) {
        if (leagueService.simulateCurrentWeekByLeagueId(leagueId))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/all/{leagueId}")
    public ResponseEntity<List<Team>> simulateEntireSeason(@PathVariable Long leagueId) {
        while (leagueService.simulateCurrentWeekByLeagueId(leagueId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/monteCarlo/{leagueId}")
//    public ResponseEntity<Map<String, Integer>> calculateMonteCarlo(@PathVariable Long leagueId) {
//        return new ResponseEntity<>(leagueService.monteCarloSimulation(leagueId, 2), HttpStatus.OK);
//
//    }
}
