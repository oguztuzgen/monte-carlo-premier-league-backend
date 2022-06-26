package com.tuzgen.montecarlopremierleague.controllers;

import com.tuzgen.montecarlopremierleague.services.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/simulation")
@RestController
public class SimulationController {

    private final LeagueService leagueService;

    public SimulationController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @PostMapping("/single/{leagueId}")
    public ResponseEntity<String> simulateSingleWeekInLeague(@PathVariable Long leagueId) {
        try {
            leagueService.simulateCurrentWeekByLeagueId(leagueId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
