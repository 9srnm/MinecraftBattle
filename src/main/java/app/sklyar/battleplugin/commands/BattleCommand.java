package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BattleCommand implements CommandExecutor {

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
                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    scheduler.cancelTasks(BattlePlugin.getInstance());
                    parameters.changeGameDay(0);
                    parameters.changeGameRuns(false);
                    player.getWorld().getWorldBorder().reset();
                    player.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                }
                else if (parameters.getGameRuns()) {
                    player.sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't run this command while the Battle is ongoing");
                }
                else if (secondaryCommand.equalsIgnoreCase("start")) {
                    parameters.changeGameRuns(true);

                    player.getWorld().getWorldBorder().setCenter(player.getWorld().getSpawnLocation());
                    player.getWorld().getWorldBorder().setSize(parameters.getBorderLength());

                    player.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                    player.getWorld().setTime(23999);

                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    scheduler.runTaskTimer(BattlePlugin.getInstance(), () -> {
                        if (!parameters.getGameRuns()) {
                            return;
                        }
                        else {
                            long timeNow = player.getWorld().getTime();
                            if (timeNow < parameters.getPreviousTime()) {
                                parameters.changeGameDay(parameters.getGameDay() + 1);
                                player.sendMessage(parameters.getGameDay() + " day");
                                if (parameters.getGameDay() == 5) {
                                    player.getWorld().getWorldBorder().setSize(1, parameters.getBorderShrinkTime());
                                }
                            }
                            parameters.changePreviousTime(timeNow);
                            int speed;
                            if (timeNow < 12000) {
                                speed = 12000 / parameters.getDayLength();
                            }
                            else {
                                speed = 12000 / parameters.getNightLength();
                            }
                            player.getWorld().setTime(player.getWorld().getTime() + speed);
                        }
                    }, 0, 20);
                }
                else if (secondaryCommand.equalsIgnoreCase("teams")) {
                    if (args.length > 1) {
                        String teamAction = args[1];
                        if (teamAction.equalsIgnoreCase("addTeam")) {
                            if (args.length > 2) {
                                String teamName = args[2];
                                if (scoreboard.getTeam(teamName) != null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Team already exists");
                                }
                                else {
                                    scoreboard.registerNewTeam(teamName);
                                    Team team = scoreboard.getTeam(teamName);
                                    team.setAllowFriendlyFire(false);
                                    team.setCanSeeFriendlyInvisibles(true);
                                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " added");
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("delTeam")) {
                            if (args.length > 2) {
                                String teamName = args[2];
                                if (scoreboard.getTeam(teamName) == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Team does not exist");
                                }
                                else {
                                    scoreboard.getTeam(teamName).unregister();
                                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " has been deleted");
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("addPlayer")) {
                            if (args.length > 3) {
                                String playerName = args[2];
                                String teamName = args[3];
                                if (scoreboard.getTeam(teamName) == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Team does not exist");
                                }
                                else {
                                    Team team = scoreboard.getTeam(teamName);
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    if (target != null) {
                                        addToTeam(target, team);
                                        player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was added to team " + ChatColor.BOLD + teamName);
                                    }
                                    else {
                                        player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Player does not exist ");
                                    }
                                }
                            } else isPromptCorrect = false;
                        }
                        else if (teamAction.equalsIgnoreCase("delPlayer")) {
                            if (args.length > 2) {
                                String playerName = args[2];
                                Player target = Bukkit.getPlayerExact(playerName);
                                if (target == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Player does not exist ");
                                }
                                if (target.getScoreboard().getPlayerTeam(target) == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Player is not in any team");
                                }
                                else {
                                    String team = target.getScoreboard().getPlayerTeam(target).getName();
                                    removePlayerFromTeams(target);
                                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was removed from team " + ChatColor.BOLD + team);
                                }
                            } else isPromptCorrect = false;
                        }
                        else isPromptCorrect = false;
                    }
                    else {
                        if (scoreboard.getTeams().isEmpty()) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "There are no teams yet");
                        }
                        else {
                            StringBuilder message = new StringBuilder(parameters.getPrefix() + ChatColor.GREEN + "List of teams: \n" + ChatColor.RESET);
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
                else if (secondaryCommand.equalsIgnoreCase("setSpawnRadius")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeSpawnRadius(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                }
                else if (secondaryCommand.equalsIgnoreCase("setDayLength")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeDayLength(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                }
                else if (secondaryCommand.equalsIgnoreCase("setNightLength")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeNightLength(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                }
                else if (secondaryCommand.equalsIgnoreCase("setBorderLength")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeBorderLength(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                }
                else if (secondaryCommand.equalsIgnoreCase("setBorderShrinkTime")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeBorderShrinkTime(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                }
                else {
                    player.sendMessage(parameters.getPrefix() + ChatColor.RED + "Unknown command: " + ChatColor.BOLD + secondaryCommand);
                }
            }
            else {
                isPromptCorrect = false;
            }
            if (!isPromptCorrect) {
                player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Type " + ChatColor.BOLD + "/help battle" + ChatColor.RESET + ChatColor.YELLOW + " to see how to use battle command");
            }
        }
        return true;
    }
}
