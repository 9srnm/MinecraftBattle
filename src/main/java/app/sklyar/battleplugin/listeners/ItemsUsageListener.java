package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class ItemsUsageListener implements Listener {

    @EventHandler
    public void ItemsUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() != null) {
            if (event.getItem().getItemMeta().equals(ItemManager.stormbreaker.getItemMeta())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!player.hasCooldown(event.getMaterial())) {
                        player.setCooldown(event.getMaterial(), 20);
                        Block block = player.getTargetBlock(null, 30);
                        World world = player.getWorld();
                        while (block.getType() == Material.AIR) {
                            block = world.getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                        }
                        for (int i = 0; i < 5; i++) {
                            player.getWorld().strikeLightningEffect(block.getLocation());
                        }
                        player.getWorld().createExplosion(block.getLocation(), 6, true, true, player);
                    }
                }
            }
        }
    }

}