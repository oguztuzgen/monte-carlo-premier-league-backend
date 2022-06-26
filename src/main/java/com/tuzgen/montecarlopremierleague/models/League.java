package com.tuzgen.montecarlopremierleague.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leagues")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class League extends BaseEntity {
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Team> teams = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Week> weeks = new ArrayList<>();

    private Integer currentWeekNo = 0;

    private Boolean isLeagueFinished = false;

    public League(String name) {
        this.name = name;
        this.currentWeekNo = 0;
    }

    public League(League league) {
        this.name = league.name;
        this.currentWeekNo = league.currentWeekNo;
        this.teams = new ArrayList<>();
        for (Team t : league.teams) {
            teams.add(new Team(t));
        }
        this.weeks = new ArrayList<>();
        for (Week w : league.weeks) {
            this.weeks.add(new Week(w));
        }
        this.isLeagueFinished = league.isLeagueFinished;
    }

    public League(String name, List<Team> teams) {
        this.name = name;
        this.teams = teams;
        this.currentWeekNo = 0;
    }

    public Week getCurrentWeek() {
        return weeks.get(currentWeekNo);
    }

    public Team getFirstTeam() {
        Team winner = null;
        for (Team t : teams) {
            if (winner == null)
                winner = t;
            if (t.getPoints() > winner.getPoints())
                winner = t;
        }

        return winner;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Team team : teams) {
            str.append(team.toString());
        }

        return name + "\n" + "Week: " + currentWeekNo + "\n" + str + "\n" + weeks.get(0);
    }
}
