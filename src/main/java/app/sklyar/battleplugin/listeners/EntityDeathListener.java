package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;


public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityType entityType = event.getEntityType();
        double dropChance = 0.05;

        if (entityType == EntityType.ZOMBIE || entityType == EntityType.SKELETON || entityType == EntityType.SPIDER || entityType == EntityType.CREEPER) {
            Random random = new Random();
            if (random.nextDouble() < dropChance) {
                ItemStack emerald = new ItemStack(Material.EMERALD);
                Item item = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), emerald);
                item.setPickupDelay(2);
            }
        }
    }
}