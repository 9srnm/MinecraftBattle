package app.sklyar.battleplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class ChestExplodeListener implements Listener {
    private final int x;
    private final int y;
    private final int z;
    public ChestExplodeListener(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @EventHandler
    public void onChestOpen(BlockExplodeEvent e) {
        if (e.getBlock().equals(e.getBlock().getWorld().getBlockAt(this.x, this.y, this.z)) && e.getBlock().getType().equals(Material.CHEST)) {
            e.setCancelled(true);
        }
        else e.setCancelled(false);
    }
}
