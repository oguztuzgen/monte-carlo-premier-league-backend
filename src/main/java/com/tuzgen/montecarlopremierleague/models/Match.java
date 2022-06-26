package com.tuzgen.montecarlopremierleague.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "matches")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match extends BaseEntity {
    @OneToOne
    private Team homeTeam;

    @OneToOne
    private Team awayTeam;
    private Integer homeScore;
    private Integer awayScore;

    public Match(Team home, Team away) {
        this.homeTeam = home;
        this.awayTeam = away;
    }

    public Match(Match toClone) {
        this.homeTeam = toClone.homeTeam;
        this.awayTeam = toClone.awayTeam;
        this.homeScore= toClone.homeScore;
        this.awayScore = toClone.awayScore;
    }

    @Override
    public String toString() {
        return
                homeTeam.getName() + ": " + homeScore + " - " +
                awayTeam.getName() + ": " + awayScore + "\n";
    }

    public void setScore(Integer home, Integer away) {
        if (home < 0 || away < 0)
            throw new RuntimeException("Score cannot be a negative number");
        this.homeScore = home;
        this.awayScore = away;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Match match = (Match) o;
        return Objects.equals(homeTeam, match.homeTeam) && Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), homeTeam, awayTeam);
    }

    public boolean isSymmetricEqual(Match right) {
        return
                (this.awayTeam.equals(right.awayTeam) &&
                        this.homeTeam.equals(right.homeTeam))
                ||
                (this.homeTeam.equals(right.awayTeam) &&
                        this.awayTeam.equals(right.homeTeam));
    }
}
