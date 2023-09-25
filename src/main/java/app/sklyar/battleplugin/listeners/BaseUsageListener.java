package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.inventories.ShopInventory;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BaseUsageListener implements Listener {

    private final List<Base> baseList;
    private final Parameters parameters;

    public BaseUsageListener(List<Base> baseList, Parameters parameters) {
        this.baseList = baseList;
        this.parameters = parameters;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void spawnBlockEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
        for(Base base : baseList){
            Double x = base.loc.getX();
            Double y = base.loc.getY();
            Double z = base.loc.getZ();
            if (block.getLocation().equals(new Location(event.getPlayer().getWorld(), x - 6, y, z + 9)) ||
                    block.getLocation().equals(new Location(event.getPlayer().getWorld(), x - 6, y + 1, z + 9))){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    @Deprecated
    public void storeUsage(PlayerInteractEvent event) {
        if (parameters.getGameRuns()) {
            Player player = event.getPlayer();
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
                    event.setCancelled(true);
                    Base playersBase = null;
                    for (Base target : baseList) {
                        if (target.loc.equals(event.getClickedBlock().getLocation())) {
                            playersBase = target;
                        }
                    }
                    if (playersBase.isUnbreakable) {
                        player.sendMessage(parameters.getPrefix() + ChatColor.RED + "The base is on 30 sec cooldown");
                    }
                    else {
                        if (playersBase.name.equals(player.getScoreboard().getPlayerTeam(player).getName())) {
                            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't break your own flag");
                        } else {
                            for (String p :
                                    player.getScoreboard().getTeam(playersBase.name).getEntries()) {
                                for (PotionEffect effect :
                                        Bukkit.getPlayer(p).getActivePotionEffects()) {
                                    Bukkit.getPlayer(p).removePotionEffect(effect.getType());
                                }
                            }
                            playersBase.setLvl(playersBase.baseLvl - 1);
                            if (playersBase.baseLvl > 1) {
                                for (String p : player.getScoreboard().getTeam(playersBase.name).getEntries()) {
                                    for (PotionEffect effect :
                                            playersBase.effects[playersBase.baseLvl - 2]) {
                                        Bukkit.getPlayer(p).addPotionEffect(effect);
                                    }
                                }
                            }

                            if (playersBase.baseLvl == 0) {
                                event.getClickedBlock().setType(Material.AIR);
                                ItemStack baseItem = ItemManager.base.clone();
                                ItemMeta baseMeta = baseItem.getItemMeta();
                                baseMeta.setDisplayName(playersBase.name);
                                baseItem.setItemMeta(baseMeta);
                                player.getInventory().addItem(baseItem);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false, false));
                                for(String playerName : player.getScoreboard().getTeam(playersBase.name).getEntries()){
                                    Bukkit.getPlayer(playerName).sendTitle(ChatColor.YELLOW + "БАЗА РАЗРУШЕНА", "Разрушитель подсвечивается белым", 10, 60, 10);
                                }
                            } else {
                                playersBase.isUnbreakable = true;
                                final Base[] plBase = {playersBase};
                                BukkitScheduler scheduler = Bukkit.getScheduler();
                                scheduler.scheduleSyncDelayedTask(BattlePlugin.getInstance(), () -> {
                                    plBase[0].isUnbreakable = false;
                                }, 20 * 30);

                                int coins = playersBase.lvlCosts[playersBase.baseLvl - 1];
                                player.getInventory().addItem(new ItemStack(Material.EMERALD, coins / 2));
                            }
                            player.getWorld().strikeLightningEffect(event.getClickedBlock().getLocation());
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (parameters.getGameRuns()) {
            Player player = event.getPlayer();
            Block block = event.getBlockPlaced();
            Material placedBlockType = event.getBlockPlaced().getType();
            if (placedBlockType == Material.END_PORTAL_FRAME) {
                if (parameters.getGameDay() == 1) {
                    if (player.getScoreboard().getPlayerTeam(player) == null) return;
                    baseList.add(new Base(player.getScoreboard().getPlayerTeam(player).getName(), block.getLocation()));
                    BattlePlugin.getInstance().schematics(System.getProperty("user.dir") + "/schematics/Base.schem", player.getWorld(), block.getX() - 7, block.getY() - 1, block.getZ() - 5);
                    player.getWorld().getBlockAt(block.getLocation()).setType(Material.END_PORTAL_FRAME);
                } else {
                    block.setType(Material.AIR);
                    ItemStack itemStack = event.getItemInHand();
                    itemStack.setAmount(1);
                    player.getInventory().remove(Material.END_PORTAL_FRAME);
                    player.getInventory().addItem(event.getItemInHand());
                }
            }
        }
    }

    @EventHandler
    public void onFlagBreak(BlockBreakEvent e) {
        if (parameters.getGameRuns()) {
            if (e.getBlock().getType().equals(Material.END_PORTAL_FRAME) || e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, 1, 0)).getType().equals(Material.END_PORTAL_FRAME)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFlagExplode(BlockExplodeEvent e) {
        if (parameters.getGameRuns()) {
            System.out.println(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, 1, 0)).getType().toString());
            if (e.getBlock().getType().equals(Material.END_PORTAL_FRAME) || e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, 1, 0)).getType().equals(Material.END_PORTAL_FRAME)) {
                e.setCancelled(true);
            }
        }
    }
}