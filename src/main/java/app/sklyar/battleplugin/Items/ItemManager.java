package app.sklyar.battleplugin.Items;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    public  static ItemStack magicWand;

    public static void init() {
        createMagicWand();
    }

    private static void createMagicWand() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Elder Wand");
        List<String> lore = new ArrayList<>();
        lore.add("§7The best artifact of the Deathly Hallows");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.LUCK, 3, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        //AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", -3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        //meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        item.setItemMeta(meta);
        magicWand = item;
    }
}
