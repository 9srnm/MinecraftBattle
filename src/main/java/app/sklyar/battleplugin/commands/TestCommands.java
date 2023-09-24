package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.inventories.BaseInventory;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            BaseInventory gui = new BaseInventory();
            player.openInventory(gui.getInventory());
        }

        return true;
    }
}
