package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.classes.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleCommand implements CommandExecutor {
    private String prefix = "" + ChatColor.AQUA + ChatColor.BOLD + "[BATTLE] " + ChatColor.RESET;
    private List<Team> teams = new ArrayList<Team>();

    private String getTeamName(String[] strings, int from) {
        List<String> words = new ArrayList<>(Arrays.asList(strings));
        words = words.subList(from, words.size());
        String teamName = String.join(" ", words);
        return teamName;
    }
    private int getTeamIndex(String teamName) {
        int count = -1;
        for (Team team:
                teams) {
            count += 1;
            if (team.getName().equals(teamName)) {
                return count;
            }
        }
        return -1;
    }

    public List<Team> getTeams() {
        return teams;
    }

    private int getPlayerTeamIndex(String playerName) {
        int count = -1;
        for (Team team :
                teams) {
            count += 1;
            List<String> teamPlayers = new ArrayList<>(Arrays.asList(team.getPlayers()));
            if (teamPlayers.contains(playerName)) {
                return count;
            }
        }
        return -1;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            boolean isPromptCorrect = true;
            Player player = ((Player) commandSender).getPlayer();
            if (player == null) return false;
            if (strings.length > 0) {
                String secondaryCommand = strings[0];
                if (secondaryCommand.equalsIgnoreCase("start")) {
                    System.out.println("battle start");
                }
                else if (secondaryCommand.equalsIgnoreCase("teams")) {
                    if (strings.length > 1) {
                        String teamAction = strings[1];
                        if (teamAction.equalsIgnoreCase("addTeam")) {
                            if (strings.length > 2) {
                                String teamName = getTeamName(strings, 2);
                                int teamIndex = getTeamIndex(teamName);
                                boolean isTeamExists = teamIndex != -1;
                                if (isTeamExists) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Team already exists");
                                }
                                else {
                                    Team newTeam = new Team(teamName);
                                    teams.add(newTeam);
                                    player.sendMessage(prefix + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " added");
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("delTeam")) {
                            if (strings.length > 2) {
                                String teamName = getTeamName(strings, 2);
                                int teamIndex = getTeamIndex(teamName);
                                boolean isTeamExists = teamIndex != -1;
                                if (!isTeamExists) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Team does not exist");
                                }
                                else {
                                    teams.remove(teamIndex);
                                    player.sendMessage(prefix + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " has been deleted");
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("addPlayer")) {
                            if (strings.length > 3) {
                                String playerName = strings[2];
                                String teamName = getTeamName(strings, 3);
                                int teamIndex = getTeamIndex(teamName);
                                boolean isTeamExists = teamIndex != -1;
                                if (!isTeamExists) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Team does not exist");
                                }
                                else {
                                    int playerTeamIndex = getPlayerTeamIndex(playerName);
                                    if (playerTeamIndex == -1) {
                                        teams.get(teamIndex).addPlayer(playerName);
                                        player.sendMessage(prefix + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was added to team " + ChatColor.BOLD + teamName);
                                    }
                                    else {
                                        player.sendMessage(prefix + ChatColor.YELLOW + "Player is already in team " + ChatColor.BOLD + teams.get(playerTeamIndex).getName());
                                    }
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("delPlayer")) {
                            if (strings.length > 2) {
                                String playerName = strings[2];
                                int playerTeamIndex = getPlayerTeamIndex(playerName);
                                if (playerTeamIndex == -1) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Player is not in any team");
                                }
                                else {
                                    teams.get(playerTeamIndex).removePlayer(playerName);
                                    player.sendMessage(prefix + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was removed from team " + ChatColor.BOLD + teams.get(playerTeamIndex).getName());
                                }
                            } else isPromptCorrect = false;
                        }
                        else isPromptCorrect = false;
                    }
                    else {
                        if (teams.isEmpty()) {
                            player.sendMessage(prefix + ChatColor.YELLOW + "There are no teams yet");
                        }
                        else {
                            StringBuilder message = new StringBuilder(prefix + ChatColor.GREEN + "List of teams: \n" + ChatColor.RESET);
                            for (Team team :
                                    teams) {
                                message.append(ChatColor.BOLD).append(team.getName()).append(": ");
                                if (team.getPlayers().length == 0) message.append("Currently no players\n");
                                else {
                                    for (String playerName :
                                            team.getPlayers()) {
                                        message.append(ChatColor.RESET).append(playerName).append(", ");
                                    }
                                    message = new StringBuilder(message.substring(0, message.length() - 2));
                                    message.append("\n");
                                }
                            }
                            message = new StringBuilder(message.substring(0, message.length() - 1));
                            player.sendMessage(message.toString());
                        }
                    }
                }
                else {
                    player.sendMessage(prefix + ChatColor.RED + "Unknown command: " + ChatColor.BOLD + secondaryCommand);
                }
            }
            else {
                isPromptCorrect = false;
            }
            if (!isPromptCorrect) {
                player.sendMessage(prefix + ChatColor.YELLOW + "Type " + ChatColor.BOLD + "/help battle" + ChatColor.RESET + ChatColor.YELLOW + " to see how to use battle command");
            }
        }
        return true;
    }
}
