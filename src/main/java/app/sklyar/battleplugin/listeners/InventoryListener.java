package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.nio.channels.SelectableChannel;
import java.util.HashMap;

public class InventoryListener implements Listener {

    private final HashMap<ItemStack, Integer> shopItemsLvl2;

    private final HashMap<ItemStack, Integer> shopItemsLvl1;
    public InventoryListener(HashMap<ItemStack, Integer> shopItemsLvl1, HashMap<ItemStack, Integer> shopItemsLvl2) {
        this.shopItemsLvl1 = shopItemsLvl1;
        this.shopItemsLvl2 = shopItemsLvl2;
    }
    @EventHandler
    public void ShopListener(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) { return; }
        if (event.getClickedInventory().getHolder() instanceof ShopInventory){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){
                ItemStack item = event.getCurrentItem();
                HashMap<ItemStack, Integer> shopItems;
                if (shopItemsLvl1.containsKey(item)) {
                    shopItems = shopItemsLvl1;
                    Integer itemCost = shopItems.get(item);
                    Integer coinsCount = 0;
                    for(ItemStack itemStack : player.getInventory().getContents()){
                        if (itemStack != null && itemStack == ItemManager.coinlvl1){
                            coinsCount += itemStack.getAmount();
                        }
                    }
                    if (coinsCount >= itemCost){
                        player.getInventory().addItem(item);
                        for (int i = 0; i < player.getInventory().getSize(); i++) {
                            ItemStack itemToRemove = player.getInventory().getItem(i);
                            if (item != null && itemToRemove == ItemManager.coinlvl1) {
                                int amount = itemToRemove.getAmount();
                                if (amount >= itemCost) {
                                    item.setAmount(amount - itemCost);
                                    player.getInventory().setItem(i, item);
                                    break;
                                } else {
                                    itemCost -= amount;
                                    item.setAmount(0);
                                    player.getInventory().setItem(i, null);
                                }
                            }
                        }
                    }
                }
                else {
                    shopItems = shopItemsLvl2;
                    Integer itemCost = shopItems.get(item);
                    Integer coinsCount = 0;
                    for(ItemStack itemStack : player.getInventory().getContents()){
                        if (itemStack != null && itemStack == ItemManager.coinlvl2){
                            coinsCount += itemStack.getAmount();
                        }
                    }
                    if (coinsCount >= itemCost){
                        player.getInventory().addItem(item);
                        for (int i = 0; i < player.getInventory().getSize(); i++) {
                            ItemStack itemToRemove = player.getInventory().getItem(i);
                            if (item != null && itemToRemove == ItemManager.coinlvl2) {
                                int amount = itemToRemove.getAmount();
                                if (amount >= itemCost) {
                                    item.setAmount(amount - itemCost);
                                    player.getInventory().setItem(i, item);
                                    break;
                                } else {
                                    itemCost -= amount;
                                    item.setAmount(0);
                                    player.getInventory().setItem(i, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
