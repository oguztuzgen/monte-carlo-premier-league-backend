package com.tuzgen.montecarlopremierleague.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teams")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseEntity {
    private String name;
    private String logo_url;
    // between 1 and 100
    private Integer strength;

    private Integer gamesWon = 0;
    private Integer gamesLost = 0;
    private Integer gamesDrawn = 0;

    private Integer goalsFor = 0;
    private Integer goalsAgainst = 0;

    public Team(Team t) {
        this.name = t.name;
        this.logo_url = t.logo_url;
        this.strength = t.strength;
        this.gamesWon = t.gamesWon;
        this.gamesLost = t.gamesLost;
        this.gamesDrawn = t.gamesDrawn;
        this.goalsFor = t.goalsFor;
        this.goalsAgainst =t.goalsAgainst;
    }

    public Integer getPoints() {
        return gamesWon*3+gamesDrawn;
    }

    public void increaseGamesWon() {
        gamesWon++;
    }

    public void increaseGamesLost() {
        gamesLost++;
    }

    public void increaseGamesDrawn() {
        gamesDrawn++;
    }

    public void updateGoals(int goalsFor, int goalsAgainst) {
        this.goalsFor += goalsFor;
        this.goalsAgainst += goalsAgainst;
    }

    public Integer getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    @Override
    public String toString() {
        return  name + "\n" +
                "W L D GF GD\n" +
                + gamesWon + " "
                + gamesLost + " "
                + gamesDrawn + " "
                 + goalsFor + " "
                 + goalsAgainst + "\n";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Team team = (Team) o;
        return name.equals(team.name);
    }

    public Team(String name, String logo_url) {
        this.name = name;
        this.logo_url = logo_url;
    }
}
