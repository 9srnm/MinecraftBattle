package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.UUID;


public class WandUsageListener implements Listener {

    private final CooldownManager cooldownManager = new CooldownManager();

    @EventHandler
    public void WandUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                UUID playerId = player.getUniqueId();
                Duration timeLeft = cooldownManager.getRemainingCooldown(playerId);
                if (timeLeft.isZero() || timeLeft.isNegative()) {
                    if (event.getItem().getItemMeta().equals(ItemManager.magicWand.getItemMeta())) {
                        Block block = player.getTargetBlock(null, 30);
                        World world = player.getWorld();
                        while (block.getType() == Material.AIR) {
                            block = world.getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                        }
                        for (int i = 0; i < 5; i++) {
                            player.getWorld().strikeLightning(block.getLocation());
                        }
                        cooldownManager.setCooldown(playerId, Duration.ofSeconds(10));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + timeLeft.toString() + " milliseconds before you can use this feature again.");
                }
            }
        }
    }
}