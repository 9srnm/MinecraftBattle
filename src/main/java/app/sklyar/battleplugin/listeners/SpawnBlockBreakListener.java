package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SpawnBlockBreakListener implements Listener {
    private final Parameters parameters;
    public SpawnBlockBreakListener(Parameters params) {
        parameters = params;
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        if (parameters.getGameDay() != 0) {
            Block block = e.getBlock();
            Location spawnPoint = e.getPlayer().getWorld().getSpawnLocation();
            if (Math.abs(spawnPoint.getX() - block.getX()) < parameters.getSpawnRadius() / 2.0 &&
                Math.abs(spawnPoint.getZ() - block.getZ()) < parameters.getSpawnRadius() / 2.0) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't break blocks on spawn");
            }
            else {
                e.setCancelled(false);
            }
        }
    }
}
