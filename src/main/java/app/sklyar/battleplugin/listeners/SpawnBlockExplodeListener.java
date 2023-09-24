package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;

public class SpawnBlockExplodeListener implements Listener {
    private final Parameters parameters;
    public SpawnBlockExplodeListener(Parameters params) {
        parameters = params;
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        if (parameters.getGameDay() != 0) {
            Block block = e.getBlock();
            Location spawnPoint = e.getBlock().getWorld().getSpawnLocation();
            if (Math.abs(spawnPoint.getX() - block.getX()) < parameters.getSpawnRadius() / 2.0 &&
                    Math.abs(spawnPoint.getZ() - block.getZ()) < parameters.getSpawnRadius() / 2.0) {
                e.setCancelled(true);
            }
            else {
                e.setCancelled(false);
            }
        }
    }
}
