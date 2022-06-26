package com.tuzgen.montecarlopremierleague.services.jpa;

import com.tuzgen.montecarlopremierleague.models.Match;
import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.models.Week;
import com.tuzgen.montecarlopremierleague.services.LeagueService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan("com.tuzgen.montecarlopremierleague.services.jpa")
class JpaLeagueServiceTest {
    List<Team> teams = new ArrayList<>() {{
        add(new Team("Arsenal", "https://upload.wikimedia.org/wikipedia/tr/9/92/Arsenal_Football_Club.png"));
        add(new Team("Manchester United", "https://upload.wikimedia.org/wikipedia/tr/b/b6/Manchester_United_FC_logo.png"));
        add(new Team("Bournemouth", "https://logodownload.org/wp-content/uploads/2019/10/bournemouth-fc-logo-1.png"));
        add(new Team("Everton", "https://upload.wikimedia.org/wikipedia/tr/7/79/Everton_Logo.png"));
    }};

    @Autowired
    private JpaLeagueService leagueService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGenerateSchedule() {
        List<Week> schedule = leagueService.generateSchedule(teams);

        // no team has two games in a week
        for (Week w : schedule) {
            List<Team> teamsVisited = new ArrayList<>();
            for (Match m: w.getMatches()) {
                assertFalse(teamsVisited.contains(m.getAwayTeam()) || teamsVisited.contains(m.getHomeTeam()));
                teamsVisited.add(m.getHomeTeam());
                teamsVisited.add(m.getAwayTeam());
            }
        }
    }
}