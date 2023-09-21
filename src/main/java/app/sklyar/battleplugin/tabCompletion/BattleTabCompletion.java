package app.sklyar.battleplugin.tabCompletion;

import app.sklyar.battleplugin.commands.BattleCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleTabCompletion implements TabCompleter {
    private final Scoreboard scoreboard;

    public BattleTabCompletion(Scoreboard s) {
        scoreboard = s;
    }

    private List<String> removeUnnecessaryCommands(List<String> availableCommands, String written) {
        List<String> newAvailableCommands = new ArrayList<>();
        for (String command :
                availableCommands) {
            if (command.toLowerCase().startsWith(written.toLowerCase())) {
                newAvailableCommands.add(command);
            }
        }
        return newAvailableCommands;
    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> availableCommands = new ArrayList<>();
        if (strings.length == 1) {
            String written = strings[0];
            availableCommands = Arrays.asList("start", "teams", "setSpawnRadius", "setDayLength", "setNightLength", "setBorderLength", "setBorderShrinkTime");
            availableCommands = removeUnnecessaryCommands(availableCommands, written);
        }
        else if (strings.length == 2) {
            String secondaryCommand = strings[0];
            String written = strings[1];
            if (secondaryCommand.equalsIgnoreCase("teams")) {
                availableCommands = Arrays.asList("addTeam", "delTeam", "addPlayer", "delPlayer");
            }
            else if (secondaryCommand.equalsIgnoreCase("setSpawnRadius")) {
                availableCommands = Arrays.asList("100");
            }
            else if (secondaryCommand.equalsIgnoreCase("setDayLength")) {
                availableCommands = Arrays.asList("1200");
            }
            else if (secondaryCommand.equalsIgnoreCase("setNightLength")) {
                availableCommands = Arrays.asList("600");
            }
            else if (secondaryCommand.equalsIgnoreCase("setBorderLength")) {
                availableCommands = Arrays.asList("2000");
            }
            else if (secondaryCommand.equalsIgnoreCase("setBorderShrinkTime")) {
                availableCommands = Arrays.asList("600");
            }
            availableCommands = removeUnnecessaryCommands(availableCommands, written);
        }
        else if (strings.length == 3) {
            String secondaryCommand = strings[0];
            if (secondaryCommand.equalsIgnoreCase("teams")) {
                String teamAction = strings[1];
                String written = strings[2];
                if (teamAction.equalsIgnoreCase("delTeam")) {
                    List<String> teams = new ArrayList<String>();
                    for (Team team :
                            scoreboard.getTeams()) {
                        teams.add(team.getName());
                    }
                    availableCommands = teams;
                } else if (teamAction.equalsIgnoreCase("addPlayer")) {;
                    List<String> players = new ArrayList<String>();
                    for (Player player :
                            Bukkit.getServer().getOnlinePlayers()) {
                        if (scoreboard.getEntryTeam(player.getName()) != null) {
                            players.add(player.getName());
                        }
                    }
                    availableCommands = players;
                } else if (teamAction.equalsIgnoreCase("delPlayer")) {
                    List<String> teams = new ArrayList<String>();
                    teams.addAll(scoreboard.getEntries());
                    availableCommands = teams;
                }
                availableCommands = removeUnnecessaryCommands(availableCommands, written);
            }
        }
        else if (strings.length == 4) {
            String secondaryCommand = strings[0];
            if (secondaryCommand.equalsIgnoreCase("teams")) {
                String teamAction = strings[1];
                String written = strings[3];
                if (teamAction.equalsIgnoreCase("addPlayer")) {
                    List<String> teams = new ArrayList<String>();
                    for (Team team:
                        scoreboard.getTeams()) {
                        teams.add(team.getName());
                    }
                    availableCommands = teams;
                }
                availableCommands = removeUnnecessaryCommands(availableCommands, written);
            }
        }
        return !availableCommands.isEmpty() ? availableCommands : null;
    }
}
