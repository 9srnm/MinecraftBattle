package app.sklyar.battleplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropFlagListener implements Listener {

    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        if (droppedItem.getType() == Material.END_PORTAL_FRAME) {
            event.setCancelled(true);
        }
    }
}
