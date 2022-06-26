package com.tuzgen.montecarlopremierleague.utils;

import com.tuzgen.montecarlopremierleague.models.Team;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public static List<String[]> readCsv(String path) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        List<String[]> parsedData = new ArrayList<>();

        String row;
        while ((row = csvReader.readLine()) != null) {
            parsedData.add(row.split(","));
        }

        csvReader.close();

        return parsedData;
    }

    public static List<Team> createTeamsFromResourceCsv() {
        try {
            List<String[]> parsedData = readCsv("src/main/resources/data/EPL_Standings.csv");

            List<Team> teams = new ArrayList<>() {{
                add(new Team("Manchester United", "https://upload.wikimedia.org/wikipedia/tr/b/b6/Manchester_United_FC_logo.png"));
                add(new Team("Arsenal", "https://upload.wikimedia.org/wikipedia/tr/9/92/Arsenal_Football_Club.png"));
                add(new Team("Tottenham Hotspur", "https://hif.wikipedia.org/wiki/file:Tottenham_Hotspur.png"));
                add(new Team("Everton", "https://upload.wikimedia.org/wikipedia/tr/7/79/Everton_Logo.png"));
            }};
            for (Team t: teams) {
                int totalPoints = 0;
                for (String[] line: parsedData) {
                    if (t.getName().equals(line[2])) {
                        totalPoints += Integer.parseInt(line[4])*3+Integer.parseInt(line[5]);
                    }
                }
                t.setStrength(totalPoints/7);
            }

            return teams;
        } catch (IOException exception) {
            System.out.println(exception.toString());
            return new ArrayList<Team>();
        }
    }
}
