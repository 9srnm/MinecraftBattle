package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestOpenListener implements Listener {
    private final int x;
    private final int y;
    private final int z;
    private final Parameters parameters;
    public ChestOpenListener(int x, int y, int z, Parameters parameters) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.parameters = parameters;
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().equals(e.getPlayer().getWorld().getBlockAt(this.x, this.y, this.z)) && e.getClickedBlock().getType().equals(Material.CHEST)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't open the chest yet");
        }
        else e.setCancelled(false);
    }
}
