package com.tuzgen.montecarlopremierleague.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weeks")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Week extends BaseEntity {
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Match match : matches) {
            str.append(match.toString());
        }

        return "------\n" + str + "------\n";
    }

    @OneToMany
    private List<Match> matches = new ArrayList<>();
}
