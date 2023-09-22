package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final Parameters parameters;
    public BlockBreakListener(Parameters params) {
        parameters = params;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
//        e.getPlayer().sendMessage(parameters.getGameDay().toString());
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
