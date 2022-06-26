package com.tuzgen.montecarlopremierleague.services.jpa;

import com.tuzgen.montecarlopremierleague.models.League;
import com.tuzgen.montecarlopremierleague.models.Match;
import com.tuzgen.montecarlopremierleague.models.Team;
import com.tuzgen.montecarlopremierleague.models.Week;
import com.tuzgen.montecarlopremierleague.repositories.LeagueRepository;
import com.tuzgen.montecarlopremierleague.repositories.MatchRepository;
import com.tuzgen.montecarlopremierleague.repositories.TeamRepository;
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
    private final TeamRepository teamRepository;

    public JpaLeagueService(MatchRepository matchRepository, LeagueRepository leagueRepository, WeekRepository weekRepository, MatchService matchService, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.leagueRepository = leagueRepository;
        this.weekRepository = weekRepository;
        this.matchService = matchService;
        this.teamRepository = teamRepository;
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
    public boolean simulateCurrentWeek(League league) {
        if (league.getIsLeagueFinished()) {
            return false;
        }
        Week currentWeek = league.getCurrentWeek();

        for (Match match: currentWeek.getMatches()) {
            matchService.playMatch(match);
        }

        System.out.println(currentWeek);
        if (league.getCurrentWeekNo() == 5) {
            league.setIsLeagueFinished(true);
        } else {
            league.setCurrentWeekNo(league.getCurrentWeekNo()+1);
        }
        leagueRepository.save(league);
        System.out.println(league.getCurrentWeekNo());
        return true;
    }

    @Override
    public boolean simulateCurrentWeekByLeagueId(Long id) {
        League league = leagueRepository.findById(id).orElseThrow(RuntimeException::new);
        return simulateCurrentWeek(league);
    }

    @Override
    public void simulateAll(League league) {
        for (Week week: league.getWeeks()) {
            simulateCurrentWeek(league);
        }
    }

    @Override
    public void simulateAllByLeagueId(Long id) {
        League league = leagueRepository.findById(id).orElseThrow(RuntimeException::new);
        simulateAll(league);
    }

    @Override
    public Map<String, Integer> monteCarloSimulation(Long leagueId, Integer epochs) {
        League league = leagueRepository.findById(leagueId).orElseThrow(RuntimeException::new);
        Map<String, Integer> winCounts = new HashMap<>();

        for (Team team: league.getTeams()) {
            winCounts.put(team.getName(), 0);
        }

        for (int i = 0; i < epochs; i++) {
            League l = new League(league);
            teamRepository.saveAll(l.getTeams());
            matchRepository.saveAll(getListOfMatchesFromSchedule(l.getWeeks()));
            weekRepository.saveAll(l.getWeeks());
            simulateAll(l);
            Team winner = l.getFirstTeam();
            winCounts.put(winner.getName(), winCounts.get(winner.getName())+1);
        }

        return winCounts;
    }


    private List<Match> getListOfMatchesFromSchedule(List<Week> schedule) {
        List<Match> matches = new ArrayList<>();

        for (Week w : schedule)
            matches.addAll(w.getMatches());

        return matches;
    }
}
