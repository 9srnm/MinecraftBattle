package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerDamageListener implements Listener {
    private final Scoreboard scoreboard;
    private final Parameters parameters;

    public PlayerDamageListener(Scoreboard s, Parameters p) {
        scoreboard = s;
        parameters = p;
    }

    @EventHandler
    public void onPlayerGetDamage(EntityDamageEvent e) {
        if (parameters.getGameRuns() && e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getHealth() - e.getDamage() <= 0) {
                e.setCancelled(true);
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                for(ItemStack itemStack : player.getInventory().getContents()){
                    if (itemStack != null && (itemStack.getType() == ItemManager.coinlvl1.getType() || itemStack.getType()
                            == ItemManager.coinlvl2.getType() || itemStack.getType() == Material.DIAMOND || itemStack.getType() == Material.IRON_INGOT ||
                            itemStack.getType() == Material.EMERALD)) {
                        itemStack.setAmount(0);
                        Item item = player.getWorld().dropItem(player.getLocation(), itemStack);
                        item.setPickupDelay(2);
                    }
                }
                player.setGameMode(GameMode.SPECTATOR);
                player.getWorld().strikeLightningEffect(player.getLocation());

                int sectorsAmount = Integer.highestOneBit(scoreboard.getTeams().size() - 1) * 2;
                int zLength = Math.pow((int) Math.sqrt(sectorsAmount), 2) == sectorsAmount ? (int) Math.sqrt(sectorsAmount) : (int) Math.sqrt(sectorsAmount / 2);
                int xLength = sectorsAmount / zLength;
                final int[] coordinates = {0, 0};
                Team[] teams = new Team[scoreboard.getTeams().size()];
                scoreboard.getTeams().toArray(teams);
                for (int i = 0; i < teams.length; i++) {
                    if (teams[i].equals(scoreboard.getPlayerTeam(player))) {
                        int zSector = i / xLength;
                        int xSector = i % xLength;
                        Location spawn = player.getWorld().getSpawnLocation();
                        coordinates[0] = (int) spawn.getX() - (int) player.getWorld().getWorldBorder().getSize() / 2 + parameters.getBorderLength() * xSector / xLength + parameters.getBorderLength() / (2 * xLength);
                        coordinates[1] = (int) spawn.getZ() - (int) player.getWorld().getWorldBorder().getSize() / 2 + parameters.getBorderLength() * zSector / zLength + parameters.getBorderLength() / (2 * zLength);
                        break;
                    }
                }
                int coordinateX = coordinates[0], coordinateZ = coordinates[1];
                player.teleport(player.getWorld().getHighestBlockAt(coordinateX, coordinateZ).getLocation().add(0, 1, 0));

                BukkitScheduler scheduler = Bukkit.getScheduler();
                final int[] taskId = new int[]{-1};
                taskId[0] = scheduler.scheduleSyncRepeatingTask(BattlePlugin.getInstance(), new Runnable() {
                    int i = 0;
                    final int deathCooldownTime = 30;
                    @Override
                    public void run() {
                        i++;
                        int fadeIn = 0, fadeOut = 0;
                        if (i == 1) fadeIn = 5;
                        if (i == 30) fadeOut = 5;
                        player.sendTitle("" + ChatColor.BOLD + ChatColor.RED + (deathCooldownTime - i),  ChatColor.YELLOW+ "до респавна", fadeIn, 20 - fadeIn - fadeOut, fadeOut);
                        if (i == 30) {
                            player.teleport(player.getWorld().getHighestBlockAt(coordinateX, coordinateZ).getLocation().add(0, 1, 0));
                            player.setGameMode(GameMode.SURVIVAL);
                            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                            scheduler.cancelTask(taskId[0]);
                        }
                    }
                }, 0, 20);

                double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

                if (playerMaxHealth == 2) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setMaxHealth(playerMaxHealth - 1);
                } else {
                    player.setMaxHealth(playerMaxHealth - 2);
                }
                if (player.getKiller() == null) {
                    double minHealth = 100;
                    Player minPlayer = null;
                    Team player_team = scoreboard.getEntryTeam(player.getName());
                    for (Team team : scoreboard.getTeams()) {
                        if (!(team.equals(player_team))) {
                            for (String targetName : team.getEntries()) {
                                Player target = Bukkit.getPlayerExact(targetName);
                                if (target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < minHealth) {
                                    minHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                                    minPlayer = target;
                                }
                            }
                        }
                    }
                    if (minPlayer != null) {
                        double minPlayerMaxHealth = minPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        minPlayer.setMaxHealth(minPlayerMaxHealth + 2);
                    }


                    return;
                }
                else if (player.getKiller().getType() == EntityType.PLAYER) {
                    Player killer = player.getKiller();
                    double killerMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    killer.setMaxHealth(killerMaxHealth + 2);
                }
            }
            else e.setCancelled(false);
        }
    }
}
