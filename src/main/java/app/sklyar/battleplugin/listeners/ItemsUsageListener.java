package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ItemsUsageListener implements Listener {

    private final List<Base> baseList;

    public ItemsUsageListener(List<Base> baseList) {
        this.baseList = baseList;
    }

    @EventHandler
    @Deprecated
    public void ItemsUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() != null) {
            if (event.getItem().getItemMeta().equals(ItemManager.stormbreaker.getItemMeta())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!player.hasCooldown(event.getMaterial())) {
                        player.setCooldown(event.getMaterial(), 20);
                        Block block = player.getTargetBlock(null, 30);
                        World world = player.getWorld();
                        while (block.getType() == Material.AIR) {
                            block = world.getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                        }
                        for (int i = 0; i < 5; i++) {
                            player.getWorld().strikeLightningEffect(block.getLocation());
                        }
                        player.getWorld().createExplosion(block.getLocation(), 6, true, true, player);
                    }
                }
            }
            if (event.getItem().getItemMeta().equals(ItemManager.elderwand.getItemMeta())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!player.hasCooldown(event.getMaterial())) {
                        player.setCooldown(event.getMaterial(), 20);
                        Block block = player.getTargetBlock(null, 50);
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            if (player.hasLineOfSight(target) && target != player && !(player.getScoreboard().getPlayerTeam(player).equals(target.getScoreboard().getPlayerTeam(target)))) {
                                double dis1 = target.getLocation().distance(player.getLocation());
                                double dis2 = target.getLocation().distance(block.getLocation());
                                double dis3 = player.getLocation().distance(block.getLocation());
                                if (dis1 + dis2 - dis3 <= 0.2){
                                    target.damage(200, player);
                                    player.getWorld().strikeLightningEffect(target.getLocation());
                                    Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " used Avada Kedavra !!!");

                                    Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                                    double distance = 0;
                                    Location currentLoc = player.getLocation().clone();
                                    System.out.println(123);
                                    while (distance < player.getLocation().distance(target.getLocation())) {
                                        currentLoc.add(direction);
                                        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, currentLoc, 5, 0, 0, 0, 0);
                                        distance += 1;
                                    }
                                    break;
                                }
                            }
                        }
                        //player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 100, 0.2, 0.2, 0.2);
                        //player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getEyeLocation(), 100, 0.2, 0.2, 0.2);
                        //player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation(), 100, 0.2, 0.2, 0.2);
                        double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        if (playerMaxHealth <= 4){
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2);
                            player.setHealth(0);
                        }
                        else{
                            player.setMaxHealth(playerMaxHealth - 4);
                        }
                    }
                }
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (player.getAttackCooldown() == 1) {
                        Block block = player.getTargetBlock(null, 30);
                        World world = player.getWorld();
                        while (block.getType() == Material.AIR) {
                            block = world.getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                        }

                        Location destination = new Location(player.getWorld(), block.getX(), block.getY() + 1, block.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                        player.teleport(destination);

                    }
                }
            }
            if (event.getItem().getItemMeta().equals(ItemManager.excalibur.getItemMeta())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!player.hasCooldown(event.getMaterial())) {
                        player.setCooldown(event.getMaterial(), 20);
                        Location loc = player.getLocation();
                        double x = loc.getX();
                        double y = loc.getY();
                        double z = loc.getZ();
                        double[] new_z = {2, 2, 2, 0, 0, -2, -2, -2};
                        double[] new_x = {-2, 0, 2, 2, 2, 0, -2, -2};
                        for(int i = 0; i < 8; i++){
                            player.getWorld().createExplosion(new Location(player.getWorld(), x + new_x[i], y, z + new_z[i]), 2, true, true, player);
                        }
                        for (int i = 0; i < 5; i++) {
                            player.getWorld().strikeLightningEffect(player.getLocation());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            if (player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.excalibur.getItemMeta())){
                Random random = new Random();
                int randomNumber = random.nextInt(100 + 1);
                if (randomNumber <= 4){
                    player.getWorld().createExplosion(damaged.getLocation(), 3, true, true, damager);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.ARROW) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player shooter = (Player) event.getEntity().getShooter();
                ItemStack bow = shooter.getInventory().getItemInMainHand();

                if (bow.getItemMeta().equals(ItemManager.robinsbow.getItemMeta())) {
                    event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerConsumePotion(PlayerItemConsumeEvent event) {
        if (event.getItem() != null && event.getItem().getItemMeta().equals(ItemManager.teleportationpotion.getItemMeta())) {
            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
            Player player = event.getPlayer();
            String teamName = (player.getScoreboard().getEntryTeam(player.getName()).getName());
            for(Base base : baseList){
                if (base.name.equalsIgnoreCase(teamName)){
                    player.teleport(base.loc.add(-6, 0, 9));
                    break;
                }
            }
        }
    }


}