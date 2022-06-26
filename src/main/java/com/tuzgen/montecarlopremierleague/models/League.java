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

    private Integer currentWeekNo = 1;

    private Integer finalWeekNo = 6;

    public League(String name) {
        this.name = name;
        this.currentWeekNo = 1;
    }

    public League(String name, List<Team> teams) {
        this.name = name;
        this.teams = teams;
        this.currentWeekNo = 1;
    }

    public Week getCurrentWeek() {
        return weeks.get(currentWeekNo-1);
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
