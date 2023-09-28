package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopListener implements Listener {

    private final HashMap<ItemStack, Integer> shopItemsLvl2;
    private final HashMap<ItemStack, Integer> shopItemsLvl1;
    public ShopListener(HashMap<ItemStack, Integer> shopItemsLvl1, HashMap<ItemStack, Integer> shopItemsLvl2) {
        this.shopItemsLvl1 = shopItemsLvl1;
        this.shopItemsLvl2 = shopItemsLvl2;
    }
    @EventHandler
    public void onShopClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }
        if (event.getClickedInventory().getHolder() instanceof ShopInventory){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){
                ItemStack item = event.getCurrentItem().clone();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(lore.size() - 1);
                meta.setLore(lore);
                item.setItemMeta(meta);
                meta.setLore(lore);
                HashMap<ItemStack, Integer> shopItems;
                if (shopItemsLvl1.containsKey(item)) {
                    shopItems = shopItemsLvl1;
                    Integer itemCost = shopItems.get(item);
                    Integer coinsCount = 0;
                    for(ItemStack itemStack : player.getInventory().getContents()){
                        if (itemStack != null && itemStack.getType() == ItemManager.coinlvl1.getType()){
                            coinsCount += itemStack.getAmount();
                        }
                    }
                    if (coinsCount >= itemCost){
                        if (item.getType() == Material.ENCHANTED_GOLDEN_APPLE){
                            double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            if (playerMaxHealth <= 18) { player.setMaxHealth(playerMaxHealth + 2); }
                            else { return; }
                        }
                        else if (item.getType() == Material.COMPASS){
                            BukkitScheduler scheduler = Bukkit.getScheduler();
                            scheduler.runTaskTimer(BattlePlugin.getInstance(), CompassUpdater(player), 0, 20);
                            player.getInventory().addItem(item);
                        }
                        else{ player.getInventory().addItem(item); }
                        ItemStack[] inventory = player.getInventory().getContents();
                        for (ItemStack target : inventory) {
                            if (target != null && target.getType().toString().equalsIgnoreCase(ItemManager.coinlvl1.getType().toString())) {
                                if (target.getAmount() >= itemCost) {
                                    target.setAmount(target.getAmount() - itemCost);
                                    break;
                                }
                                else{
                                    itemCost -= target.getAmount();
                                    target.setAmount(target.getAmount() - target.getAmount());
                                }
                            }
                        }
                    }
                }
                else {
                    shopItems = shopItemsLvl2;
                    Integer itemCost = shopItems.get(item);
                    Integer coinsCount = 0;
                    for(ItemStack itemStack : player.getInventory().getContents()){
                        if (itemStack != null && itemStack.getType() == ItemManager.coinlvl2.getType()){
                            coinsCount += itemStack.getAmount();
                        }
                    }
                    if (coinsCount >= itemCost){
                        if (item.getType() == Material.BOW){
                            player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                            player.getInventory().addItem(ItemManager.robinsbow);
                        }
                        else if (item.getType() == Material.NETHERITE_CHESTPLATE){
                            Team team = player.getScoreboard().getEntryTeam(player.getName());
                            for(String targetName : team.getEntries()){
                                Player target = Bukkit.getPlayer(targetName);
                                ItemStack armor1 = new ItemStack(Material.NETHERITE_HELMET, 1);
                                ItemMeta meta1 = armor1.getItemMeta();
                                meta1.setUnbreakable(true);
                                armor1.setItemMeta(meta1);
                                target.getInventory().addItem(armor1);
                                ItemStack armor2 = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
                                ItemMeta meta2 = armor2.getItemMeta();
                                meta2.setUnbreakable(true);
                                armor2.setItemMeta(meta2);
                                target.getInventory().addItem(armor2);
                                ItemStack armor3 = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
                                ItemMeta meta3 = armor3.getItemMeta();
                                meta3.setUnbreakable(true);
                                armor3.setItemMeta(meta3);
                                armor3.getItemMeta().setUnbreakable(true);
                                target.getInventory().addItem(armor3);
                                ItemStack armor4 = new ItemStack(Material.NETHERITE_BOOTS, 1);
                                ItemMeta meta4 = armor4.getItemMeta();
                                meta4.setUnbreakable(true);
                                armor4.setItemMeta(meta4);
                                target.getInventory().addItem(armor4);
                            }
                        }
                        else { player.getInventory().addItem(item); }
                        ItemStack[] inventory = player.getInventory().getContents();
                        for (ItemStack target : inventory) {
                            if (target != null && target.getType().toString().equalsIgnoreCase(ItemManager.coinlvl2.getType().toString())) {
                                if (target.getAmount() >= itemCost) {
                                    target.setAmount(target.getAmount() - itemCost);
                                    break;
                                }
                                else{
                                    itemCost -= target.getAmount();
                                    target.setAmount(target.getAmount() - target.getAmount());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Runnable CompassUpdater (Player player) {

        return () -> {
            Player target = findNearestPlayer(player);
            if (target != null) {
                Location targetLocation = target.getLocation();
                player.setCompassTarget(targetLocation);
            }
        };
    }

    @Deprecated
    private Player findNearestPlayer(Player player) {
        Player nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        Location loc1 = player.getLocation();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getScoreboard().getPlayerTeam(onlinePlayer) == null || player.getScoreboard().getPlayerTeam(onlinePlayer) == null) { break; }
            if (!(player.getScoreboard().getPlayerTeam(player).equals(onlinePlayer.getScoreboard().getPlayerTeam(onlinePlayer)))) {
                Location loc2 = onlinePlayer.getLocation();
                double distance = loc1.distance(loc2);
                if (distance < nearestDistance) {
                    nearest = onlinePlayer;
                    nearestDistance = distance;
                }
            }
        }

        return nearest;
    }
}
