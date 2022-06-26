package com.tuzgen.montecarlopremierleague.services.jpa;

import com.tuzgen.montecarlopremierleague.models.Match;
import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.repositories.MatchRepository;
import com.tuzgen.montecarlopremierleague.repositories.TeamRepository;
import com.tuzgen.montecarlopremierleague.repositories.WeekRepository;
import com.tuzgen.montecarlopremierleague.services.MatchService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class JpaMatchService implements MatchService {
    private final MatchRepository matchRepository;
    private final WeekRepository weekRepository;

    private final TeamRepository teamRepository;

    public JpaMatchService(MatchRepository matchRepository, WeekRepository weekRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.weekRepository = weekRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void playMatch(Match match) {
        Team home = match.getHomeTeam();
        Team away = match.getAwayTeam();

        match.setHomeScore(getGoalsFromRNG(ThreadLocalRandom.current().nextInt(0, home.getStrength())));
        match.setAwayScore(getGoalsFromRNG(ThreadLocalRandom.current().nextInt(0, away.getStrength())));

        updateStandings(match);
        System.out.println(match);
        teamRepository.save(home);
        teamRepository.save(away);
        matchRepository.save(match);
    }

    private void updateStandings(Match match) {
        Team home = match.getHomeTeam();
        Team away = match.getAwayTeam();

        home.updateGoals(match.getHomeScore(), match.getAwayScore());
        away.updateGoals(match.getAwayScore(), match.getHomeScore());

        if (match.getHomeScore() > match.getAwayScore()) {
            home.increaseGamesWon();
            away.increaseGamesLost();
        } else if (match.getHomeScore() < match.getAwayScore()) {
            home.increaseGamesLost();
            away.increaseGamesWon();
        } else {
            home.increaseGamesDrawn();
            away.increaseGamesDrawn();
        }
    }

    private Integer getGoalsFromRNG(Integer rng) {
        if (0 <= rng && rng < 20)
            return 0;
        else if (20 <= rng && rng < 35)
            return 1;
        else if (35 <= rng && rng < 50)
            return 2;
        else if (50 <= rng && rng < 65)
            return 3;
        else if (65 <= rng && rng < 100)
            return ThreadLocalRandom.current().nextInt(3, 7);
        else return -1;
    }
}
