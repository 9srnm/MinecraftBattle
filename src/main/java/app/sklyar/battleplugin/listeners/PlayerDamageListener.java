package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Random;

public class PlayerDamageListener implements Listener {
    private final Scoreboard scoreboard;
    private final Parameters parameters;
    private final List<Base> baseList;

    public PlayerDamageListener(Scoreboard s, Parameters p, List<Base> bl) {
        scoreboard = s;
        parameters = p;
        baseList = bl;
    }

    @EventHandler
    public void onPlayerGetDamage(PlayerDeathEvent e) {
        if (parameters.getGameRuns()) {
            Player player = e.getEntity();
            player.getWorld().strikeLightningEffect(player.getLocation());
            for(ItemStack itemStack : player.getInventory().getContents()){
                if (itemStack != null && (itemStack.getType() == ItemManager.coinlvl1.getType() || itemStack.getType()
                        == ItemManager.coinlvl2.getType() || itemStack.getType() == Material.DIAMOND || itemStack.getType() == Material.IRON_INGOT ||
                        itemStack.getType() == Material.EMERALD)) {
                    Item item = player.getWorld().dropItem(player.getLocation(), itemStack);
                    itemStack.setAmount(0);
                    item.setPickupDelay(2);
                }
                else if (itemStack != null && (itemStack.getType() == Material.END_PORTAL_FRAME)){
                    if (!(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§6Base"))){
                        String baseName = itemStack.getItemMeta().getDisplayName();
                        itemStack.setAmount(0);
                        player.removePotionEffect(PotionEffectType.GLOWING);
                        Base base = null;
                        for(Base target : baseList){
                            if (target.name.equalsIgnoreCase(baseName)){
                                base = target;
                                break;
                            }
                        }
                        Location loc = base.loc;
                        base.setLvl(1);
                        player.getWorld().getBlockAt(loc).setType(ItemManager.base.getType());
                    }
                }
            }
            player.setGameMode(GameMode.SPECTATOR);

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
            Base[] playerBase = {null};
            for (Base base :
                    baseList) {
                if (base.name.equals(scoreboard.getPlayerTeam(player).getName())) {
                    playerBase[0] = base;
                    break;
                }
            }
            int coordinateX = coordinates[0], coordinateZ = coordinates[1];
            player.teleport(player.getWorld().getHighestBlockAt(coordinateX, coordinateZ).getLocation().clone().add(0, 1, 0));

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
                    if (!parameters.getGameRuns()) {
                        scheduler.cancelTask(taskId[0]);
                    }
                    player.sendTitle("" + ChatColor.BOLD + ChatColor.RED + (deathCooldownTime - i),  ChatColor.YELLOW+ "до респавна", fadeIn, 20 - fadeIn - fadeOut, fadeOut);
                    if (i == 30) {
                        if (playerBase[0] == null) player.teleport(player.getWorld().getHighestBlockAt(coordinateX, coordinateZ).getLocation().clone().add(0, 1, 0));
                        else {
                            player.setNoDamageTicks(200);
                            if (playerBase[0].baseLvl > 1) {
                                for (PotionEffect pe :
                                        playerBase[0].effects[playerBase[0].baseLvl - 2]) {
                                    player.addPotionEffect(pe);
                                }
                            }
                        }
                        if (parameters.getGameDay() > 6) {
                            player.teleport(player.getWorld().getHighestBlockAt(coordinateX, coordinateZ).getLocation().clone().add(0, 1, 0));
                        }
                        else {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.teleport(new Location(player.getWorld(), playerBase[0].loc.getX() - 6, playerBase[0].loc.getY(), playerBase[0].loc.getZ() + 9));
                        }
                        scheduler.cancelTask(taskId[0]);
                    }
                }
            }, 0, 20);

            double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

            if (playerMaxHealth == 2 || (playerBase[0] != null && !(playerBase[0].baseRespawn))) {
                player.setGameMode(GameMode.SPECTATOR);
                player.setMaxHealth(1);
            } else {
                player.setMaxHealth(playerMaxHealth - 2);
            }
            if (player.getKiller() == null || !player.getKiller().getType().equals(EntityType.PLAYER) || player.equals(player.getKiller())) {
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
            }
            else if (player.getKiller().getType() == EntityType.PLAYER) {
                Player killer = player.getKiller();
                double killerMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                killer.setMaxHealth(killerMaxHealth + 2);
            }

            int countAlive = 0;
            Team teamAlive = null;
            for (Team team :
                    scoreboard.getTeams()) {
                boolean isTeamDead = true;
                for (String p :
                        team.getEntries()) {
                    if (!Bukkit.getPlayer(p).getGameMode().equals(GameMode.SPECTATOR) || Bukkit.getPlayer(p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() != 1) {
                        isTeamDead = false;
                    }
                }
                if (!isTeamDead) {
                    countAlive += 1;
                    teamAlive = team;
                }
            }
            if (countAlive == 1) {
                parameters.changeGameRuns(false);
                for (Player p :
                        Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.sendTitle(ChatColor.YELLOW + teamAlive.getName(), "победитель зарубы", 10, 180, 10);
                    p.teleport(p.getWorld().getSpawnLocation().clone().add(0, 20, 0));
                }
                for (String p :
                        teamAlive.getEntries()) {
                    Bukkit.getPlayer(p).setGameMode(GameMode.SURVIVAL);
                    Bukkit.getPlayer(p).teleport(Bukkit.getPlayer(p).getWorld().getSpawnLocation());
                }
                scheduler.runTaskTimer(BattlePlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Random rand = new Random();
                        int x = rand.nextInt(-10, 10) + (int) player.getWorld().getSpawnLocation().getX();
                        int z = rand.nextInt(-10, 10) + (int) player.getWorld().getSpawnLocation().getZ();
                        Location loc = player.getWorld().getHighestBlockAt(x, z).getLocation();
                        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                        FireworkMeta fwm = fw.getFireworkMeta();

                        fwm.setPower(2);
                        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

                        fw.setFireworkMeta(fwm);
                        fw.detonate();

                    }
                }, 0, 20);
            }
        }
    }
}
