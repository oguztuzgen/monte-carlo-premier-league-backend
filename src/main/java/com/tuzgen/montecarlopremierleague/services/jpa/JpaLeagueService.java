package com.tuzgen.montecarlopremierleague.services.jpa;

import com.tuzgen.montecarlopremierleague.models.League;
import com.tuzgen.montecarlopremierleague.models.Match;
import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.models.Week;
import com.tuzgen.montecarlopremierleague.repositories.LeagueRepository;
import com.tuzgen.montecarlopremierleague.repositories.MatchRepository;
import com.tuzgen.montecarlopremierleague.repositories.WeekRepository;
import com.tuzgen.montecarlopremierleague.services.LeagueService;
import com.tuzgen.montecarlopremierleague.services.MatchService;
import com.tuzgen.montecarlopremierleague.utils.CsvParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JpaLeagueService implements LeagueService {
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final WeekRepository weekRepository;
    private final MatchService matchService;

    public JpaLeagueService(MatchRepository matchRepository, LeagueRepository leagueRepository, WeekRepository weekRepository, MatchService matchService) {
        this.matchRepository = matchRepository;
        this.leagueRepository = leagueRepository;
        this.weekRepository = weekRepository;
        this.matchService = matchService;
    }

    @Override
    public List<Week> generateSchedule(List<Team> teams) {
        List<Week> schedule = new ArrayList<>();
        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < (teams.size() - 1)*2; i++)
            schedule.add(new Week());

        // generate the first week
        for (int j = 0; j < teams.size()/2; j++) {
            matches.add(new Match(teams.get(j), teams.get(teams.size()-1-j)));
        }
        schedule.get(0).setMatches(new ArrayList<>(matches));

        // generate first half of the season
        Queue<Team> rotationQueue = new LinkedList<>();
        for (int i = 1; i < schedule.size()/2; i++) {
            matches = new ArrayList<>();

            for (Match m: schedule.get(i-1).getMatches())
                matches.add(new Match(m));

            for (int j = 0; j < matches.size(); j++) {
                rotationQueue.add(matches.get(j).getAwayTeam());
                if (j == 0) continue;
                matches.get(j).setAwayTeam(rotationQueue.remove());
            }
            rotationQueue.add(matches.get(matches.size()-1).getHomeTeam());
            matches.get(matches.size()-1).setHomeTeam(rotationQueue.remove());
            matches.get(0).setAwayTeam(rotationQueue.remove());

            schedule.get(i).setMatches(new ArrayList<>(matches));
        }

        // generate the second half of the league
        for (int i = schedule.size()/2; i < schedule.size(); i++) {
            List<Match> secondHalfMatches = new ArrayList<>();
            for (Match m: schedule.get(i-(schedule.size()/2)).getMatches()) {
                Match newMatch = new Match(m);
                Team temp = newMatch.getAwayTeam();
                newMatch.setAwayTeam(newMatch.getHomeTeam());
                newMatch.setHomeTeam(temp);
                secondHalfMatches.add(newMatch);
            }

            schedule.get(i).setMatches(secondHalfMatches);
        }
        System.out.println(schedule);
        return schedule;
    }

    @Override
    public League createWithTeams(List<Team> teams) {
        CsvParser.createTeamsFromResourceCsv();

        League newLeague = new League("Premier League 2022-2023");
        newLeague.setTeams(teams);

        List<Week> weeks = generateSchedule(teams);
        newLeague.setWeeks(weeks);

        matchRepository.saveAll(getListOfMatchesFromSchedule(weeks));
        weekRepository.saveAll(weeks);
        return leagueRepository.save(newLeague);
    }

    @Override
    public void simulateCurrentWeek(League league) {
        if (league.getCurrentWeekNo() > league.getTeams().size() * 2) {
            throw new RuntimeException("League has ended");
        }
        Week currentWeek = league.getCurrentWeek();

        for (Match match: currentWeek.getMatches()) {
            matchService.playMatch(match);
        }

        System.out.println(currentWeek);
        league.setCurrentWeekNo(league.getCurrentWeekNo()+1);
    }

    @Override
    public void simulateCurrentWeekByLeagueId(Long id) {
        League league = leagueRepository.findById(id).orElseThrow(RuntimeException::new);
        simulateCurrentWeek(league);
    }

    @Override
    public void simulateAll(League league) {

    }

    @Override
    public void simulateAllByLeagueId(Long id) {

    }


    private List<Match> getListOfMatchesFromSchedule(List<Week> schedule) {
        List<Match> matches = new ArrayList<>();

        for (Week w : schedule)
            matches.addAll(w.getMatches());

        return matches;
    }
}
