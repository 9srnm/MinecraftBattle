package app.sklyar.battleplugin.classes;


import java.util.ArrayList;
import java.util.List;

public class Team {
    private final String name;
    private final List<String> players = new ArrayList<String>();

    public Team (String teamName) {
        name = teamName;
    }


    public String getName() {
        return name;
    }

    public String[] getPlayers() {
        String[] playersArray = new String[players.size()];
        players.toArray(playersArray);
        return playersArray;
    }

    public boolean addPlayer(String playerName) {
        if (!players.contains(playerName)) {
            players.add(playerName);
            return true;
        }
        else return false;
    }

    public boolean removePlayer(String playerName) {
        if (players.contains(playerName)) {
            players.remove(playerName);
            return true;
        }
        else return false;
    }
}
