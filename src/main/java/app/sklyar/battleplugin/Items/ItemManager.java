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
    public static ItemStack stonespear;
    public static ItemStack ironspear;
    public static ItemStack diamondspear;
    public static ItemStack excalibur;
    public static ItemStack coinlvl1;
    public static ItemStack coinlvl2;
    public static ItemStack healthhealer;
    public static ItemStack robinsbow;


    public static void init() {
        createStormbreaker();
        createElderWand();
        createSSpear();
        createISpear();
        createDSpear();
        createExcalibur();
        createCoinLvl1();
        createCoinLvl2();
        createHealthHealer();
        createRobinsBow();
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
        meta.addEnchant(Enchantment.DAMAGE_ALL, 3, false);
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
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.setUnbreakable(true);
        AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", -3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        item.setItemMeta(meta);
        elderwand = item;
    }

    private static void createSSpear() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6STONE_SPEAR");
        AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1024, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        AttributeModifier damage = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", -1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damage);
        item.setItemMeta(meta);
        stonespear = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("sspear"), item);
        sr.shape("ISI",
                " S ",
                " S ");
        sr.setIngredient('I', Material.COBBLESTONE);
        sr.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(sr);
    }

    private static void createISpear() {
        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6IRON_SPEAR");
        AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1024, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        AttributeModifier damage = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", -1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damage);
        item.setItemMeta(meta);
        ironspear = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("ispear"), item);
        sr.shape("ISI",
                " S ",
                " S ");
        sr.setIngredient('I', Material.IRON_INGOT);
        sr.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(sr);
    }

    private static void createDSpear() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6DIAMOND_SPEAR");
        AttributeModifier attackSpeed = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1024, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        AttributeModifier damage = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", -1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damage);
        item.setItemMeta(meta);
        diamondspear = item;
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("dspear"), item);
        sr.shape("ISI",
                " S ",
                " S ");
        sr.setIngredient('I', Material.DIAMOND);
        sr.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(sr);
    }

    private static void createExcalibur() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Excalibur");
        List<String> lore = new ArrayList<>();
        lore.add("§2 The mythical sword of King Arthur");
        lore.add("§2 that may be attributed with magical powers");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 3, false);
        //AttributeModifier damage = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 70, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        //meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damage);
        item.setItemMeta(meta);
        excalibur = item;

    }

    private static void createCoinLvl1() {
        ItemStack item = new ItemStack(Material.GHAST_TEAR, 64);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6CoinLvl1");
        List<String> lore = new ArrayList<>();
        lore.add("§2Coin lvl 1 for shop");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.LUCK, 3, false);
        item.setItemMeta(meta);
        coinlvl1 = item;

    }

    private static void createCoinLvl2() {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 64);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6CoinLvl2");
        List<String> lore = new ArrayList<>();
        lore.add("§2Coin lvl 2 for shop");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.LUCK, 3, false);
        item.setItemMeta(meta);
        coinlvl2 = item;
    }

    private static void createHealthHealer() {
        ItemStack item = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Healt_hHealer");
        List<String> lore = new ArrayList<>();
        lore.add("§2If you have smaller then 10 hearts");
        lore.add("§2Heal you 1 heart");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.LUCK, 3, false);
        item.setItemMeta(meta);
        healthhealer = item;
    }

    private static void createRobinsBow() {
        ItemStack item = new ItemStack(Material.BOW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Robin_Hood's_Bow");
        List<String> lore = new ArrayList<>();
        lore.add("§2Еhe bow of the legendary");
        lore.add("§2fighter for justice");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 2, false);
        meta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, false);
        item.setItemMeta(meta);
        robinsbow = item;
    }
}
