package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnchantmentTableEvent implements Listener {
    private final Parameters parameters;
    public EnchantmentTableEvent(Parameters p) {
        parameters = p;
    }

    @EventHandler
    public void onEnchantmentTableClick(PlayerInteractEvent e) {
        if (parameters.getGameDay() < 5 && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            player.sendMessage(parameters.getPrefix() + ChatColor.RED + "You can't use enchantment table until day 5");
        }
        else {
            e.setCancelled(false);
        }
    }
}
