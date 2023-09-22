package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class WandUsageListener implements Listener {

    @EventHandler
    public void WandUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (!player.hasCooldown(event.getMaterial()) && event.getItem().getItemMeta().equals(ItemManager.magicWand.getItemMeta())) {
                    player.setCooldown(event.getMaterial(), 20);
                    Block block = player.getTargetBlock(null, 30);
                    World world = player.getWorld();
                    while (block.getType() == Material.AIR) {
                        block = world.getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                    }
                    for (int i = 0; i < 5; i++) {
                        player.getWorld().strikeLightningEffect(block.getLocation());
                    }
                    player.getWorld().createExplosion(block.getLocation(), 8, true, true, player);
                }
            }
        }
    }
}