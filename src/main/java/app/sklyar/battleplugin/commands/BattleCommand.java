package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.inventories.BaseInventory;
import app.sklyar.battleplugin.listeners.ChestBreakListener;
import app.sklyar.battleplugin.listeners.ChestExplodeListener;
import app.sklyar.battleplugin.listeners.ChestOpenListener;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import sun.jvm.hotspot.opto.Block;
import sun.jvm.hotspot.opto.CallJavaNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class BattleCommand implements CommandExecutor {

    private final Scoreboard scoreboard;

    private final Parameters parameters;

    private final Recipe[] diamondRequiredRecipes = new Recipe[]{
            Bukkit.getServer().getRecipe(Material.DIAMOND_BOOTS.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_AXE.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_BLOCK.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_HOE.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_CHESTPLATE.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_HELMET.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_LEGGINGS.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_PICKAXE.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_SHOVEL.getKey()),
            Bukkit.getServer().getRecipe(Material.DIAMOND_SWORD.getKey())
    };

    private final Recipe goldenAppleRecipe = Bukkit.getServer().getRecipe(Material.GOLDEN_APPLE.getKey());
    private final Recipe[] restrictedRecipes = new Recipe[]{
            Bukkit.getServer().getRecipe(Material.ENCHANTING_TABLE.getKey()),
            Bukkit.getServer().getRecipe(Material.BREWING_STAND.getKey()),
            Bukkit.getServer().getRecipe(Material.BLAZE_POWDER.getKey()),
            Bukkit.getServer().getRecipe(Material.BEACON.getKey()),
            Bukkit.getServer().getRecipe(Material.RED_BANNER.getKey()),
    };

    private final List<Base> baseList;

    public BattleCommand(Parameters p, Scoreboard s, List<Base> bl) {
        parameters = p;
        scoreboard = s;
        cleanTeams(s);
        baseList = bl;
    }

    private void cleanTeams(Scoreboard scoreboard) {
        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
    }

    private void addToTeam(Player player, Team team) {
        removePlayerFromTeams(player);
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    private void removePlayerFromTeams(Player player) {
        for (Team team : scoreboard.getTeams()) {
            team.removePlayer(player);
        }
    }

    private void removeRecipes(int day, Player player) {
        player.getServer().resetRecipes();
        if (day < 2) {
            for (Recipe diamondItem :
                    diamondRequiredRecipes) {
                player.getServer().removeRecipe(diamondItem.getResult().getType().getKey());
            }
        }
        if (day < 4) {
            player.getServer().removeRecipe(goldenAppleRecipe.getResult().getType().getKey());
        }
        for (Recipe restrictedItem :
                restrictedRecipes) {
            player.getServer().removeRecipe(restrictedItem.getResult().getType().getKey());
        }
    }

    private void sendTitleToAllDays(String title, String description) {
        for (Player p :
                Bukkit.getServer().getOnlinePlayers()) {
            int easeIn = 10, length = 100, easeOut = 10;
            p.sendTitle("" + ChatColor.BOLD + ChatColor.YELLOW + title, description, easeIn, length, easeOut);

        }
    }

    private void sendMessageToAllDays(String title, String description) {
        for (Player p :
                Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(parameters.getPrefix() + ChatColor.BOLD + ChatColor.YELLOW + title + "\n" + ChatColor.RESET + description);
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

                    player.getServer().resetRecipes();
                    player.getWorld().setGameRule(GameRule.DO_LIMITED_CRAFTING, false);

                    player.getWorld().setGameRule(GameRule.KEEP_INVENTORY, false);
                    player.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);

                    for (Player p :
                            player.getServer().getOnlinePlayers()) {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                        p.setHealth(20);
                        p.setFoodLevel(20);
                    }

                    for (PotionEffect effect :
                            player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }

                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Battle has been finished");
                } else if (parameters.getGameRuns()) {
                    player.sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't run this command while the Battle is ongoing");
                } else if (secondaryCommand.equalsIgnoreCase("start")) {
                    boolean allInTeams = true;
                    for (Player p :
                            Bukkit.getServer().getOnlinePlayers()) {
                        if (scoreboard.getPlayerTeam(p) == null) {
                            allInTeams = false;
                            break;
                        }
                    }
                    System.out.println(allInTeams);
                    boolean allTeamsHavePlayer = true;
                    for (Team team :
                            scoreboard.getTeams()) {
                        boolean hasPlayerOnline = false;
                        for (String pString :
                                team.getEntries()) {
                            Player p = Bukkit.getServer().getPlayer(pString);
                            if (p != null && p.isOnline()) {
                                hasPlayerOnline = true;
                                break;
                            }
                        }
                        if (!hasPlayerOnline) {
                            allTeamsHavePlayer = false;
                            break;
                        }
                    }
                    if (allInTeams && allTeamsHavePlayer && scoreboard.getTeams().size() > 1) {
                        for (Player p :
                                player.getServer().getOnlinePlayers()) {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            p.setHealth(20);
                            p.setFoodLevel(20);
                        }

                        parameters.changeGameRuns(true);

                        Random rand = new Random();
                        for (Team team :
                                player.getScoreboard().getTeams()) {
                            int i = rand.nextInt(team.getSize());
                            String[] teamNicks = new String[team.getSize()];
                            team.getEntries().toArray(teamNicks);
                            Player p = Bukkit.getPlayer(teamNicks[i]);
                            p.getInventory().addItem(ItemManager.base);
                        }

                        player.getWorld().setGameRule(GameRule.KEEP_INVENTORY, true);
                        player.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

                        player.getWorld().getWorldBorder().setCenter(player.getWorld().getSpawnLocation());
                        player.getWorld().getWorldBorder().setSize(parameters.getBorderLength());

                        int sectorsAmount = Integer.highestOneBit(scoreboard.getTeams().size() - 1) * 2;
                        int zLength = Math.pow((int) Math.sqrt(sectorsAmount), 2) == sectorsAmount ? (int) Math.sqrt(sectorsAmount) : (int) Math.sqrt(sectorsAmount / 2);
                        int xLength = sectorsAmount / zLength;
                        Team[] teams = new Team[scoreboard.getTeams().size()];
                        scoreboard.getTeams().toArray(teams);
                        for (int i = 0; i < teams.length; i++) {
                            int zSector = i / xLength;
                            int xSector = i % xLength;
                            Location spawn = player.getWorld().getSpawnLocation();
                            int coordinateX = (int) spawn.getX() - (int) player.getWorld().getWorldBorder().getSize() / 2 + parameters.getBorderLength() * xSector / xLength + parameters.getBorderLength() / (2 * xLength);
                            int coordinateZ = (int) spawn.getZ() - (int) player.getWorld().getWorldBorder().getSize() / 2 + parameters.getBorderLength() * zSector / zLength + parameters.getBorderLength() / (2 * zLength);
                            for (String pString :
                                    teams[i].getEntries()) {
                                Player p = Bukkit.getServer().getPlayer(pString);
                                if (p != null)
                                    p.teleport(player.getWorld().getHighestBlockAt(coordinateX, coordinateZ).getLocation().add(0, 1, 0));
                            }
                            ;
                        }

                        player.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                        player.getWorld().setTime(23999);

                        player.getWorld().setGameRule(GameRule.DO_LIMITED_CRAFTING, true);

                        BukkitScheduler scheduler = Bukkit.getScheduler();
                        scheduler.runTaskTimer(BattlePlugin.getInstance(), () -> {
                            if (!parameters.getGameRuns()) {
                                return;
                            } else {
                                long timeNow = player.getWorld().getTime();
                                if (timeNow < parameters.getPreviousTime()) {
                                    parameters.changeGameDay(parameters.getGameDay() + 1);
                                    removeRecipes(parameters.getGameDay(), player);

                                    if (parameters.getGameDay() < 6) {
                                        int[] deltaTimeForChests = new int[4];
                                        Arrays.fill(deltaTimeForChests, -1);
                                        if (parameters.getGameDay() - 1 <= deltaTimeForChests.length) {
                                            for (int i = 0; i < parameters.getGameDay() - 1; i++) {
                                                deltaTimeForChests[i] = new Random().nextInt((int) (parameters.getDayLength() * 20 * 0.1), (int) (parameters.getDayLength() * 20 * 0.7));
                                            }
                                        }
                                        int coordX = (int) player.getWorld().getSpawnLocation().getX() - (int) player.getWorld().getWorldBorder().getSize() / 2;
                                        int coordZ = (int) player.getWorld().getSpawnLocation().getZ() - (int) player.getWorld().getWorldBorder().getSize() / 2;
                                        final int[] iter = {-1};
                                        for (int i = 0; i < parameters.getGameDay() - 1; i++) {
                                            iter[0] = i;
                                            scheduler.scheduleSyncDelayedTask(BattlePlugin.getInstance(), new Runnable() {
                                                @Override
                                                public void run() {
                                                    int chestSpawnX = coordX + parameters.getBorderLength() * (iter[0] + 1) / parameters.getGameDay();
                                                    int chestSpawnZ = coordZ + parameters.getBorderLength() * (iter[0] + 1) / parameters.getGameDay();
                                                    player.getWorld().getHighestBlockAt(chestSpawnX, chestSpawnZ).getLocation().add(0, 1, 0).getBlock().setType(Material.CHEST);
                                                    int chestSpawnY = player.getWorld().getHighestBlockAt(chestSpawnX, chestSpawnZ).getY();

                                                    ChestBreakListener chestBreakListener = new ChestBreakListener(chestSpawnX, chestSpawnY, chestSpawnZ, parameters);
                                                    ChestOpenListener chestOpenListener = new ChestOpenListener(chestSpawnX, chestSpawnY, chestSpawnZ, parameters);
                                                    ChestExplodeListener chestExplodeListener = new ChestExplodeListener(chestSpawnX, chestSpawnY, chestSpawnZ);
                                                    player.getServer().getPluginManager().registerEvents(chestBreakListener, BattlePlugin.getInstance());
                                                    player.getServer().getPluginManager().registerEvents(chestOpenListener, BattlePlugin.getInstance());
                                                    player.getServer().getPluginManager().registerEvents(chestExplodeListener, BattlePlugin.getInstance());

                                                    final int[] taskId = new int[]{-1};
                                                    taskId[0] = scheduler.runTaskTimer(BattlePlugin.getInstance(), new Runnable() {
                                                        int secondsPassed = 0;

                                                        @Override
                                                        public void run() {
                                                            if (secondsPassed >= 60 * 5 - 10 && secondsPassed < 60 * 5) {
                                                                for (Player p :
                                                                        player.getServer().getOnlinePlayers()) {
                                                                    p.sendMessage(parameters.getPrefix() + ChatColor.LIGHT_PURPLE + "Таинственный сундук откроется через " + (60 * 5 - secondsPassed) + " сек.");
                                                                }
                                                            } else if (secondsPassed == 60 * 5) {
                                                                PlayerInteractEvent.getHandlerList().unregister(chestOpenListener);
                                                                Chest chest = (Chest) player.getWorld().getBlockAt(chestSpawnX, chestSpawnY, chestSpawnZ).getState();
                                                                Random rand = new Random();
                                                                int[] possibilityDistribution = new int[100];
                                                                Arrays.fill(possibilityDistribution, 0);
                                                                Arrays.fill(possibilityDistribution, 0, 25, 1);
                                                                Arrays.fill(possibilityDistribution, 25, 30, 2);
                                                                ItemStack stack;
                                                                for (int j = 0; j < 27; j++) {
                                                                    int item = possibilityDistribution[rand.nextInt(10)];
                                                                    if (item == 0) {
                                                                        continue;
                                                                    } else if (item == 1) {
                                                                        stack = new ItemStack(Material.EMERALD, 1);
                                                                        chest.getInventory().setItem(j, stack);
                                                                    } else {
                                                                        stack = ItemManager.coinlvl1;
                                                                        chest.getInventory().setItem(j, stack);
                                                                    }
                                                                }
                                                                for (Player p :
                                                                        player.getServer().getOnlinePlayers()) {
                                                                    p.sendMessage(parameters.getPrefix() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Таинственный сундук открыт!");
                                                                }
                                                            } else if (secondsPassed > 60 * 5) {
                                                                scheduler.cancelTask(taskId[0]);
                                                                scheduler.scheduleSyncDelayedTask(BattlePlugin.getInstance(), new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        BlockBreakEvent.getHandlerList().unregister(chestBreakListener);
                                                                        BlockExplodeEvent.getHandlerList().unregister(chestExplodeListener);
                                                                        player.getWorld().getBlockAt(chestSpawnX, chestSpawnY, chestSpawnZ).setType(Material.AIR);
                                                                        for (Player p :
                                                                                player.getServer().getOnlinePlayers()) {
                                                                            p.sendMessage(parameters.getPrefix() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Таинственный сундук убран!");
                                                                        }
                                                                    }
                                                                }, 20 * 60);
                                                            } else if (secondsPassed % 60 == 0) {
                                                                for (Player p :
                                                                        player.getServer().getOnlinePlayers()) {
                                                                    p.sendMessage(parameters.getPrefix() + ChatColor.BOLD + ChatColor.DARK_RED + "Таинственный сундук " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "откроется через " + ChatColor.BOLD + ChatColor.DARK_RED + (5 - secondsPassed / 60) + " мин. " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "по координатам: " + ChatColor.BOLD + ChatColor.DARK_RED + chestSpawnX + " " + chestSpawnY + " " + chestSpawnZ);
                                                                }
                                                            }
                                                            secondsPassed++;
                                                        }
                                                    }, 0, 20).getTaskId();

                                                }
                                            }, deltaTimeForChests[i]);
                                        }
                                    }

                                    if (parameters.getGameDay() < 7)
                                        player.playSound(player, Sound.ENTITY_WITHER_DEATH, 1, 1);

                                    switch (parameters.getGameDay()) {
                                        case 1:
                                            sendTitleToAllDays("НАЧАЛО ИГРЫ", "Ищите изумруды и прокачивайте базу");
                                            sendMessageToAllDays("НАЧАЛО ИГРЫ", "Установите базу. Ищите изумруды и прокачивайте её. Она даст вам эффекты и монеты для покупки интересных вещей на центре. Пока что заходить на территорию соперника нельзя");
                                            break;
                                        case 2:
                                            sendTitleToAllDays("АЛМАЗНЫЕ ПРЕДМЕТЫ", "Теперь вы можете крафтить алмазные предметы");
                                            sendMessageToAllDays("АЛМАЗНЫЕ ПРЕДМЕТЫ И ОТКРЫТИЕ ГРАНИЦ", "Теперь вы можете крафтить алмазные предметы и забегать на территорию соперника, чтобы забрать флаг, тем самым ограничив жизнь соперников до одной");
                                            break;
                                        case 3:
                                            sendTitleToAllDays("ПОДСВЕТКА БАЗЫ", "База теперь подсвечивается");
                                            sendMessageToAllDays("ЭФФЕКТЫ НА ЧУЖОЙ ПОЛОВИНЕ И ПОДСВЕТКА БАЗЫ", "Теперь эффекты, которые вы получили от базы будут действовать на половине соперника. База теперь подсвечивается");
                                            break;
                                        case 4:
                                            sendTitleToAllDays("ЗОЛОТЫЕ ЯБЛОКИ", "Можно крафтить золотые яблоки");
                                            sendMessageToAllDays("ЗОЛОТЫЕ ЯБЛОКИ", "Не хватает ПВП? Скрафти золотые яблоки и вступай в бой");
                                            break;
                                        case 5:
                                            sendTitleToAllDays("ЗАЧАРОВАНИЯ", "Теперь на центре можно зачаровать свои предметы");
                                            sendMessageToAllDays("ЗАЧАРОВАНИЯ", "Теперь на центре можно зачаровать свои предметы");
                                            break;
                                        case 6:
                                            sendTitleToAllDays("LE FIN!", "Зона сужается! У всех одна жизнь");
                                            sendMessageToAllDays("LE FIN!", "Вот и финал! Зона сужается (да-да фортнайтеры, про вас не забыли)! У всех одна жизнь! Кто кого?");
                                            player.getWorld().getWorldBorder().setSize(1, parameters.getBorderShrinkTime());
                                            break;
                                    }
                                }
                                parameters.changePreviousTime(timeNow);
                                int speed;
                                if (timeNow < 12000) {
                                    speed = 12000 / parameters.getDayLength();
                                } else {
                                    speed = 12000 / parameters.getNightLength();
                                }
                                player.getWorld().setTime(player.getWorld().getTime() + speed);
                            }
                        }, 0, 20);
                    } else
                        player.sendMessage(parameters.getPrefix() + ChatColor.RED + "Every person online should be in a team and every team should include at least one person online");
                } else if (secondaryCommand.equalsIgnoreCase("teams")) {
                    if (args.length > 1) {
                        String teamAction = args[1];
                        if (teamAction.equalsIgnoreCase("addTeam")) {
                            if (args.length > 2) {
                                String teamName = args[2];
                                if (scoreboard.getTeam(teamName) != null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Team already exists");
                                } else {
                                    scoreboard.registerNewTeam(teamName);
                                    Team team = scoreboard.getTeam(teamName);
                                    team.setAllowFriendlyFire(false);
                                    team.setCanSeeFriendlyInvisibles(true);
                                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " added");
                                }
                            } else isPromptCorrect = false;
                        } else if (teamAction.equalsIgnoreCase("delTeam")) {
                            if (args.length > 2) {
                                String teamName = args[2];
                                if (scoreboard.getTeam(teamName) == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Team does not exist");
                                } else {
                                    scoreboard.getTeam(teamName).unregister();
                                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Team " + ChatColor.BOLD + teamName + ChatColor.RESET + ChatColor.GREEN + " has been deleted");
                                }
                            } else isPromptCorrect = false;
                        } else if (teamAction.equalsIgnoreCase("addPlayer")) {
                            if (args.length > 3) {
                                String playerName = args[2];
                                String teamName = args[3];
                                if (scoreboard.getTeam(teamName) == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Team does not exist");
                                } else {
                                    Team team = scoreboard.getTeam(teamName);
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    if (target != null) {
                                        addToTeam(target, team);
                                        player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was added to team " + ChatColor.BOLD + teamName);
                                    } else {
                                        player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Player does not exist ");
                                    }
                                }
                            } else isPromptCorrect = false;
                        } else if (teamAction.equalsIgnoreCase("delPlayer")) {
                            if (args.length > 2) {
                                String playerName = args[2];
                                Player target = Bukkit.getPlayerExact(playerName);
                                if (target == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Player does not exist ");
                                }
                                if (target.getScoreboard().getPlayerTeam(target) == null) {
                                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Player is not in any team");
                                } else {
                                    String team = target.getScoreboard().getPlayerTeam(target).getName();
                                    removePlayerFromTeams(target);
                                    player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + "Player " + ChatColor.BOLD + playerName + ChatColor.RESET + ChatColor.GREEN + " was removed from team " + ChatColor.BOLD + team);
                                }
                            } else isPromptCorrect = false;
                        } else isPromptCorrect = false;
                    } else {
                        if (scoreboard.getTeams().isEmpty()) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "There are no teams yet");
                        } else {
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
                } else if (secondaryCommand.equalsIgnoreCase("setSpawnRadius")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeSpawnRadius(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                } else if (secondaryCommand.equalsIgnoreCase("setDayLength")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeDayLength(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                } else if (secondaryCommand.equalsIgnoreCase("setNightLength")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeNightLength(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                } else if (secondaryCommand.equalsIgnoreCase("setBorderLength")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeBorderLength(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                } else if (secondaryCommand.equalsIgnoreCase("setBorderShrinkTime")) {
                    if (args.length > 1) {
                        try {
                            int value = Integer.parseInt(args[1]);
                            parameters.changeBorderShrinkTime(value);
                            player.sendMessage(parameters.getPrefix() + ChatColor.GREEN + ChatColor.BOLD + secondaryCommand + ChatColor.RESET + ChatColor.GREEN + " was set to " + ChatColor.BOLD + args[1]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "This is not an integer");
                        }
                    }
                } else {
                    player.sendMessage(parameters.getPrefix() + ChatColor.RED + "Unknown command: " + ChatColor.BOLD + secondaryCommand);
                }
            } else {
                if (parameters.getGameRuns()) {
                    Base playerBase = null;
                    for (Base base :
                            baseList) {
                        if (base.name.equals(player.getScoreboard().getPlayerTeam(player).getName())) {
                            playerBase = base;
                            break;
                        }
                    }
                    if (playerBase != null) {
                        if (player.getLocation().distance(playerBase.loc) <= 32) {
                            player.openInventory(new BaseInventory().getInventory());
                        } else
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "You are too far from your base (>32 blocks)");
                    } else
                        player.sendMessage(parameters.getPrefix() + ChatColor.RED + "Your team hasn't placed the flag");
                }
                else isPromptCorrect = false;
                if (!isPromptCorrect) {
                    player.sendMessage(parameters.getPrefix() + ChatColor.YELLOW + "Type " + ChatColor.BOLD + "/help battle" + ChatColor.RESET + ChatColor.YELLOW + " to see how to use battle command");
                }
            }
        }
        return true;
    }
}
