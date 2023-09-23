package app.sklyar.battleplugin.Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    public static ItemStack stormbreaker;
    public static ItemStack elderwand;


    public static void init() {
        createStormbreaker();
        createElderWand();;
    }

    private static void createStormbreaker() {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Stormbreaker");
        List<String> lore = new ArrayList<>();
        lore.add("§7The Thunder God's Greatest Weapon");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        AttributeModifier damage = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 70, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damage);
        item.setItemMeta(meta);
        stormbreaker = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("stormbreaker"), item);
        sr.shape("IDI",
                " S ",
                " S ");
        sr.setIngredient('I', Material.IRON_INGOT);
        sr.setIngredient('D', Material.DIAMOND_BLOCK);
        sr.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(sr);
    }

    private static void createElderWand() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Elder Wand");
        List<String> lore = new ArrayList<>();
        lore.add("§7The most powerful magic wand");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", -3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        item.setItemMeta(meta);
        elderwand = item;
    }
}
