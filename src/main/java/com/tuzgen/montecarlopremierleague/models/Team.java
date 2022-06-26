package com.tuzgen.montecarlopremierleague.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teams")

@ToString
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

    public Team(String name, String logo_url) {
        this.name = name;
        this.logo_url = logo_url;
    }
}
