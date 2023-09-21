package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BattleCommand implements CommandExecutor {
    private String prefix = "" + ChatColor.AQUA + ChatColor.BOLD + "[BATTLE] " + ChatColor.RESET;

    private final Scoreboard scoreboard;

    private final Parameters parameters;

    public BattleCommand(Parameters p, Scoreboard s) {
        parameters = p;
        scoreboard = s;
        cleanTeams(s);
    }

    private void cleanTeams(Scoreboard scoreboard){
        for(Team team: scoreboard.getTeams()){
            team.unregister();
        }
    }

    private void addToTeam(Player player, Team team) {
        removePlayerFromTeams(player);
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    private void removePlayerFromTeams(Player player){
        for(Team team : scoreboard.getTeams()){
            team.removePlayer(player);
        }
    }

    @Override
    @Deprecated
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            boolean isPromptCorrect = true;
            Player player = ((Player) commandSender).getPlayer();
            if (player == null) return false;
            if (args.length > 0) {
                String secondaryCommand = args[0];
                if (secondaryCommand.equalsIgnoreCase("stop")) {
                    parameters.changeGameDay(0);
                }
                else if (secondaryCommand.equalsIgnoreCase("start")) {
                    System.out.println("battle start");
                }
                else if (secondaryCommand.equalsIgnoreCase("teams")) {
                    if (args.length > 1) {
                        String teamAction = args[1];
                        if (teamAction.equalsIgnoreCase("addTeam")) {
                            if (args.length > 2) {
                                String teamName = args[2];
                                if (scoreboard.getTeam(teamName) != null) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Team already exists");
                                }
                                else {
                                    scoreboard.registerNewTeam(teamName);
                                    player.sendMessage(prefix + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " added");
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("delTeam")) {
                            if (args.length > 2) {
                                String teamName = args[2];
                                if (scoreboard.getTeam(teamName) == null) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Team does not exist");
                                }
                                else {
                                    scoreboard.getTeam(teamName).unregister();
                                    player.sendMessage(prefix + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " has been deleted");
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("addPlayer")) {
                            if (args.length > 3) {
                                String playerName = args[2];
                                String teamName = args[3];
                                if (scoreboard.getTeam(teamName) == null) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Team does not exist");
                                }
                                else {
                                    Team team = scoreboard.getTeam(teamName);
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    if (target != null) {
                                        addToTeam(player, team);
                                        player.sendMessage(prefix + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was added to team " + ChatColor.BOLD + teamName);
                                    }
                                    else {
                                        player.sendMessage(prefix + ChatColor.YELLOW + "Player does not exist ");
                                    }
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("delPlayer")) {
                            if (args.length > 2) {
                                String playerName = args[2];
                                Player target = Bukkit.getPlayerExact(playerName);
                                if (target == null) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Player does not exist ");
                                }
                                if (target.getScoreboard().getPlayerTeam(target) == null) {
                                    player.sendMessage(prefix + ChatColor.YELLOW + "Player is not in any team");
                                }
                                else {
                                    String team = target.getScoreboard().getPlayerTeam(target).getName();
                                    removePlayerFromTeams(target);
                                    player.sendMessage(prefix + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was removed from team " + ChatColor.BOLD + team);
                                }
                            } else isPromptCorrect = false;
                        }
                        else isPromptCorrect = false;
                    }
                    else {
                        if (scoreboard.getTeams().isEmpty()) {
                            player.sendMessage(prefix + ChatColor.YELLOW + "There are no teams yet");
                        }
                        else {
                            StringBuilder message = new StringBuilder(prefix + ChatColor.GREEN + "List of teams: \n" + ChatColor.RESET);
                            for (Team team : scoreboard.getTeams()) {
                                message.append(ChatColor.BOLD).append(team.getName()).append(": ");
                                if (team.getPlayers().isEmpty()) message.append("Currently no players\n");
                                else {
                                    for (String entry : team.getEntries()) {
                                        message.append(ChatColor.RESET).append(entry).append(", ");
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
