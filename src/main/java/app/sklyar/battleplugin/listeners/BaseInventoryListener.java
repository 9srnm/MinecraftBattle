package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.inventories.BaseInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;


public class BaseInventoryListener implements Listener {

    private final List<Base> baseList;
    private final Parameters parameters;

    private Player playerMain;
    public BaseInventoryListener(List<Base> baseList, Parameters parameters) {
        this.baseList = baseList;
        this.parameters = parameters;
    }

    @EventHandler
    public void BaseListener(InventoryClickEvent event) {

        if (parameters.getGameRuns()) {
            if (event.getClickedInventory() == null) {
                return;
            }
            if (event.getClickedInventory().getHolder() instanceof BaseInventory) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                playerMain = (Player) event.getWhoClicked();
                Base base = getBase();
                if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                    if (event.getCurrentItem().getItemMeta().equals(ItemManager.buylvl.getItemMeta()) && base.baseLvl < base.maxBaseLvl) {
                        int coinsCount = 0;
                        for (ItemStack itemStack : player.getInventory().getContents()) {
                            if (itemStack != null && itemStack.getType() == Material.EMERALD) {
                                coinsCount += itemStack.getAmount();
                            }
                        }
                        Integer lvlCost = base.lvlCosts[base.baseLvl - 1];
                        if (coinsCount >= lvlCost && base.baseLvl + 1 <= base.maxBaseLvl) {
                            base.setLvl(base.baseLvl + 1);
                            for (String p :
                                    player.getScoreboard().getTeam(base.name).getEntries()) {
                                for (PotionEffect effect :
                                        Bukkit.getPlayer(p).getActivePotionEffects()) {
                                    Bukkit.getPlayer(p).removePotionEffect(effect.getType());
                                }
                            }
                            for (String p : player.getScoreboard().getTeam(base.name).getEntries()) {
                                for (PotionEffect effect :
                                         base.effects[base.baseLvl - 2]) {
                                    Bukkit.getPlayer(p).addPotionEffect(effect);
                                }
                            }
                            ItemStack[] inventory = player.getInventory().getContents();
                            for (ItemStack target : inventory) {
                                if (target != null && target.getType().toString().equalsIgnoreCase(Material.EMERALD.toString())) {
                                    if (target.getAmount() >= lvlCost) {
                                        target.setAmount(target.getAmount() - lvlCost);
                                        break;
                                    } else {
                                        lvlCost -= target.getAmount();
                                        target.setAmount(target.getAmount() - target.getAmount());
                                    }
                                }
                            }
                        }
                    } else if (event.getCurrentItem().getItemMeta().equals(ItemManager.buycoinlvl1.getItemMeta())) {
                        Integer lvlCost = 10;
                        Integer coinsCount = 0;
                        for (ItemStack itemStack : player.getInventory().getContents()) {
                            if (itemStack != null && itemStack.getType() == Material.EMERALD) {
                                coinsCount += itemStack.getAmount();
                            }
                        }
                        boolean flag = false;
                        if (base.playersHistory1.containsKey(player)) {
                            if (base.playersHistory1.get(player) == 1 && base.baseLvl >= 4) {
                                base.playersHistory1.put(player, 2);
                                flag = true;
                            } else if (base.baseLvl >= 5) {
                                base.playersHistory1.put(player, 3);
                                flag = true;
                            }
                        } else {
                            if (base.baseLvl >= 3) {
                                base.playersHistory1.put(player, 1);
                                flag = true;
                            }
                        }
                        if (coinsCount >= lvlCost && flag) {
                            player.getInventory().addItem(ItemManager.coinlvl1);
                            ItemStack[] inventory = player.getInventory().getContents();
                            for (ItemStack target : inventory) {
                                if (target != null && target.getType().toString().equalsIgnoreCase(Material.EMERALD.toString())) {
                                    if (target.getAmount() >= lvlCost) {
                                        target.setAmount(target.getAmount() - lvlCost);
                                        break;
                                    } else {
                                        lvlCost -= target.getAmount();
                                        target.setAmount(target.getAmount() - target.getAmount());
                                    }
                                }
                            }
                        }
                    } else if (event.getCurrentItem().getItemMeta().equals(ItemManager.buycoinlvl2.getItemMeta())) {
                        Integer lvlCost = 20;
                        Integer coinsCount = 0;
                        for (ItemStack itemStack : player.getInventory().getContents()) {
                            if (itemStack != null && itemStack.getType() == Material.EMERALD) {
                                coinsCount += itemStack.getAmount();
                            }
                        }
                        boolean flag = false;
                        if (base.playersHistory2.containsKey(player)) {
                            if (base.playersHistory2.get(player) == 1 && base.baseLvl >= 5) {
                                base.playersHistory2.put(player, 2);
                                flag = true;
                            }
                        } else {
                            if (base.baseLvl >= 4) {
                                base.playersHistory2.put(player, 1);
                                flag = true;
                            }
                        }
                        if (coinsCount >= lvlCost && flag) {
                            player.getInventory().addItem(ItemManager.coinlvl2);
                            ItemStack[] inventory = player.getInventory().getContents();
                            for (ItemStack target : inventory) {
                                if (target != null && target.getType().toString().equalsIgnoreCase(Material.EMERALD.toString())) {
                                    if (target.getAmount() >= lvlCost) {
                                        target.setAmount(target.getAmount() - lvlCost);
                                        break;
                                    } else {
                                        lvlCost -= target.getAmount();
                                        target.setAmount(target.getAmount() - target.getAmount());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    private Base getBase(){
        Base base = null;
        for(Base target : baseList){
            if (target.name.equalsIgnoreCase(playerMain.getScoreboard().getPlayerTeam(playerMain).getName())){
                base = target;
                break;
            }
        }

        return base;
    }

}
