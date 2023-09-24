package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class SpawnBlockPlaceListener implements Listener {
    private final Parameters parameters;
    public SpawnBlockPlaceListener(Parameters params) {
        parameters = params;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (parameters.getGameDay() != 0) {
            Block block = e.getBlock();
            Location spawnPoint = e.getPlayer().getWorld().getSpawnLocation();
            if (Math.abs(spawnPoint.getX() - block.getX()) < parameters.getSpawnRadius() / 2.0 &&
                    Math.abs(spawnPoint.getZ() - block.getZ()) < parameters.getSpawnRadius() / 2.0) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't place blocks on spawn");
            }
            else {
                e.setCancelled(false);
            }
        }
    }
}
