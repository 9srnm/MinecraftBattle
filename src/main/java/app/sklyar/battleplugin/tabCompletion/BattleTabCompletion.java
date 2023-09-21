package app.sklyar.battleplugin.tabCompletion;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.classes.Team;
import app.sklyar.battleplugin.commands.BattleCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleTabCompletion implements TabCompleter {
    private final BattleCommand battleCommand;

    public BattleTabCompletion(BattleCommand battleCmd) {
        battleCommand = battleCmd;
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
                    List<Team> teams = battleCommand.getTeams();
                    for (Team team :
                            teams) {
                        availableCommands.add(team.getName());
                    }
                } else if (teamAction.equalsIgnoreCase("addPlayer")) {
                    Player[] onlinePlayers = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(onlinePlayers);
                    List<String> addedPlayers = new ArrayList<>();
                    for (Team team :
                            battleCommand.getTeams()) {
                        addedPlayers.addAll(Arrays.asList(team.getPlayers()));
                    }
                    for (Player player :
                            onlinePlayers) {
                        if (!addedPlayers.contains(player.getName())) {
                            availableCommands.add(player.getName());
                        }
                    }

                } else if (teamAction.equalsIgnoreCase("delPlayer")) {
                    for (Team team :
                            battleCommand.getTeams()) {
                        availableCommands.addAll(Arrays.asList(team.getPlayers()));
                    }
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
                    for (Team team:
                         battleCommand.getTeams()) {
                        availableCommands.add(team.getName());
                    }
                }
                availableCommands = removeUnnecessaryCommands(availableCommands, written);
            }
        }
        return !availableCommands.isEmpty() ? availableCommands : null;
    }
}
