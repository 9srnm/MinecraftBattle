package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TrapBreakListener implements Listener {


    private final List<Entity> lst;
    public TrapBreakListener(List<Entity> lst) {
        this.lst = lst;
    }
    @EventHandler
    public void onEntityDeath(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.DEEPSLATE_REDSTONE_ORE) {
            Random random1 = new Random();
            if (random1.nextDouble() < 0.3) {
                Random random2 = new Random();
                int randomNumber = random2.nextInt(2 + 1);
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                for(int i = -3; i < 4; i++) {
                    for(int j = 0; j < 4; j++) {
                        for(int k = -3; k < 4; k++) {
                            player.getWorld().getBlockAt(new Location(player.getWorld(), x + i, y + j, z + k)).setType(Material.AIR);
                        }
                    }
                }
                if (randomNumber == 0){
                    for(int i = 0; i < 10; i++){
                        Entity entity = player.getWorld().spawnEntity(new Location(player.getWorld(), x + 1, y, z), EntityType.ZOMBIE);
                        lst.add(entity);
                    }
                }
                else if (randomNumber == 1){
                    for(int i = 0; i < 7; i++){
                        Entity entity = player.getWorld().spawnEntity(new Location(player.getWorld(), x + 1, y, z), EntityType.SPIDER);
                        lst.add(entity);
                    }
                }
                else if (randomNumber == 2){
                    for(int i = 0; i < 3; i++){
                        Entity entity = player.getWorld().spawnEntity(new Location(player.getWorld(), x + 1, y, z), EntityType.WITCH);
                        lst.add(entity);
                    }
                    for(int i = 0; i < 3; i++){
                        Entity entity = player.getWorld().spawnEntity(new Location(player.getWorld(), x + 1, y, z), EntityType.SKELETON);
                        lst.add(entity);
                    }
                }
            }
        }
    }
}