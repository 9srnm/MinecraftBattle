package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.BattlePlugin;
import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.inventories.BaseInventory;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;

public class TestCommands implements CommandExecutor {

    private final HashMap<ItemStack, Integer> shopItemsLvl2;
    private final HashMap<ItemStack, Integer> shopItemsLvl1;
    public TestCommands(HashMap<ItemStack, Integer> shopItemsLvl1, HashMap<ItemStack, Integer> shopItemsLvl2) {
        this.shopItemsLvl1 = shopItemsLvl1;
        this.shopItemsLvl2 = shopItemsLvl2;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){

        if (!(commandSender instanceof Player)){ return true; }

        Player player = (Player) commandSender;


        if (command.getName().equalsIgnoreCase("open_shop")) {

            Location location = player.getLocation();
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setArms(true);
            armorStand.setBasePlate(false);

            ItemStack sword = ItemManager.excalibur;
            armorStand.getEquipment().setItemInMainHand(sword);

            Bukkit.getScheduler().runTaskTimer(BattlePlugin.getInstance(), () -> {
                EulerAngle eulerAngle = armorStand.getRightArmPose();
                eulerAngle = eulerAngle.add(0, 0.1, 0);
                armorStand.setRightArmPose(eulerAngle);
            }, 0L, 1L);

            armorStand.setCustomName("Excalibur");
            armorStand.setCustomNameVisible(true);

            return true;
        }

        return true;
    }
}
