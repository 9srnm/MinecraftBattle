package app.sklyar.battleplugin.inventories;

import app.sklyar.battleplugin.Items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopInventory implements InventoryHolder{

    public Inventory inv;
    private final HashMap<ItemStack, Integer> shopItemsLvl2;

    private final HashMap<ItemStack, Integer> shopItemsLvl1;

    public ShopInventory(HashMap<ItemStack, Integer> shopItemsLvl1, HashMap<ItemStack, Integer> shopItemsLvl2){
        this.shopItemsLvl1 = shopItemsLvl1;
        this.shopItemsLvl2 = shopItemsLvl2;
        inv = Bukkit.createInventory(this, Math.max(shopItemsLvl2.size() + shopItemsLvl1.size(), 9), "Shop");
        init();
    }

    private void init(){
        ItemStack item;
        for(ItemStack key: shopItemsLvl1.keySet()){
            item = key.clone();
            ItemMeta meta= item.getItemMeta();
            List<String> lore = meta.getLore();
            lore.add("Cost = " + shopItemsLvl1.get(key) + " lvl 1 coins");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        for(ItemStack key: shopItemsLvl2.keySet()){
            item = key.clone();
            ItemMeta meta= item.getItemMeta();
            List<String> lore = meta.getLore();
            lore.add("Cost = " + shopItemsLvl2.get(key) + " lvl 2 coins");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.addItem(item);
        }
    }



    @Override
    public Inventory getInventory(){
        return inv;
    }

}