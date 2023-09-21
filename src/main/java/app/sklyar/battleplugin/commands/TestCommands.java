package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.Items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){

        if (!(commandSender instanceof Player)){ return true; }

        Player player = (Player) commandSender;
        player.getInventory().addItem(ItemManager.magicWand);

        return true;
    }
}
