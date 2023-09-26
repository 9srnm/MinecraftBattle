package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.inventories.BaseInventory;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ArmorStandListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void storeUsage(PlayerArmorStandManipulateEvent event){
        if (event.getArmorStandItem().getType() == Material.AIR){
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        Integer coinsCount = 0;
        for(ItemStack itemStack : player.getInventory().getContents()){
            if (itemStack != null && itemStack.getType() == ItemManager.coinlvl2.getType()){
                coinsCount += itemStack.getAmount();
            }
        }
        Integer itemCost = 3;
        if (coinsCount >= itemCost){
            for(Player target : Bukkit.getOnlinePlayers()){
                target.sendTitle(ChatColor.YELLOW + "Excalibur", player.getName() + " забрал его", 10, 60, 10);
            }
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
        else{
            event.setCancelled(true);
        }

    }
}