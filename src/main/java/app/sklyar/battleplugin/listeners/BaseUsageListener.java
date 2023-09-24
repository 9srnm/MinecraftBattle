package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BaseUsageListener implements Listener {

    private final List<Base> baseList;
    public BaseUsageListener(List<Base> baseList) {
        this.baseList = baseList;
    }


    @EventHandler
    @Deprecated
    public void storeUsage(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
                event.setCancelled(true);
                Base playersBase = null;
                for(Base target : baseList) {
                    if(target.name == player.getScoreboard().getPlayerTeam(player).getName()){
                        playersBase = target;
                    }
                }
                Integer coins = playersBase.lvlCosts[playersBase.baseLvl - 1];
                playersBase.LvlDown();
                player.getInventory().addItem(new ItemStack(Material.EMERALD, coins / 2));
            }
        }
    }


    @EventHandler
    @Deprecated
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Material placedBlockType = event.getBlockPlaced().getType();
        if (placedBlockType == Material.END_PORTAL_FRAME) {
            Integer x = block.getX();
            Integer y = block.getY();
            Integer z = block.getZ();
            for(int i = -2; i < 3; i++) {
                for(int j = 0; j < 4; j++) {
                    for(int k = -2; k < 8; k++){
                        if (i == 0 && j == 0 && k == 0) continue;
                        player.getWorld().getBlockAt(new Location(player.getWorld(), x + i, y + j, z + k)).setType(Material.AIR);
                        if (player.getScoreboard().getPlayerTeam(player) == null) { return; }
                        baseList.add(new Base(player.getScoreboard().getPlayerTeam(player).getName(), block.getLocation()));
                    }
                }
            }
            player.getWorld().getBlockAt(new Location(player.getWorld(), x, y, z + 5)).setType(Material.END_STONE);
        }
    }

}