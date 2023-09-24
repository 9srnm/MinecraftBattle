package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemsCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){

        if (!(commandSender instanceof Player)){ return true; }

        Player player = (Player) commandSender;

        if (command.getName().equalsIgnoreCase("give_stormbreaker")) {
            player.getInventory().addItem(ItemManager.stormbreaker);
            player.setMaxHealth(20);
        }

        if (command.getName().equalsIgnoreCase("give_elderwand")) {
            player.getInventory().addItem(ItemManager.elderwand);
            player.setMaxHealth(20);
        }
        if (command.getName().equalsIgnoreCase("give_excalibur")) {
            player.getInventory().addItem(ItemManager.excalibur);
            player.setMaxHealth(20);
        }

        return true;
    }
}
