package app.sklyar.battleplugin.listeners;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;


public class BlocksUsageListener implements Listener {

    private final HashMap<ItemStack, Integer> shopItemsLvl2;
    private final HashMap<ItemStack, Integer> shopItemsLvl1;
    public BlocksUsageListener(HashMap<ItemStack, Integer> shopItemsLvl1, HashMap<ItemStack, Integer> shopItemsLvl2) {
        this.shopItemsLvl1 = shopItemsLvl1;
        this.shopItemsLvl2 = shopItemsLvl2;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void storeUsage(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SHULKER_BOX) {
                //event.setCancelled(true);
                ShopInventory gui = new ShopInventory(shopItemsLvl1, shopItemsLvl2);
                player.openInventory(gui.getInventory());
                event.setCancelled(true);
            }
        }
    }
}