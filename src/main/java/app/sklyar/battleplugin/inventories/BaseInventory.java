package app.sklyar.battleplugin.inventories;

import app.sklyar.battleplugin.Items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class BaseInventory implements InventoryHolder{

    public Inventory inv;

    public BaseInventory(){
        inv = Bukkit.createInventory(this, 9, "Base");
        init();
    }

    private void init(){
        inv.addItem(ItemManager.buylvl);
        inv.addItem(ItemManager.buycoinlvl1);
        inv.addItem(ItemManager.buycoinlvl2);
    }



    @Override
    public Inventory getInventory(){
        return inv;
    }

}