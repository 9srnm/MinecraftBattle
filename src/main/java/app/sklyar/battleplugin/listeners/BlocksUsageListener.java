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
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BlocksUsageListener implements Listener {

    private final HashMap<ItemStack, Integer> shopItemsLvl2;
    private final HashMap<ItemStack, Integer> shopItemsLvl1;
    List<Base> baseList = new ArrayList<>();
    public BlocksUsageListener(HashMap<ItemStack, Integer> shopItemsLvl1, HashMap<ItemStack, Integer> shopItemsLvl2, List<Base> baseList) {
        this.shopItemsLvl1 = shopItemsLvl1;
        this.shopItemsLvl2 = shopItemsLvl2;
        this.baseList = baseList;
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
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
                Team playerTeam = player.getScoreboard().getEntryTeam(player.getName());
                for(Base playerBase : baseList){
                    if (event.getClickedBlock().getLocation().distance(playerBase.loc) <= 16){
                        if (!(playerBase.name.equalsIgnoreCase(playerTeam.getName()))){
                            event.setCancelled(true);
                        }
                    }
                }

            }
        }
    }
}