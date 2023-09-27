package app.sklyar.battleplugin.commands;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.inventories.BaseInventory;
import app.sklyar.battleplugin.inventories.ShopInventory;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemsCommands implements CommandExecutor {
    private final Parameters parameters;
    private final List<Base> baseList;
    public ItemsCommands(Parameters parameters, List<Base>baseList) {
        this.parameters = parameters;
        this.baseList = baseList;
    }

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
        if (command.getName().equalsIgnoreCase("shop")) {
            if (parameters.getGameRuns()) {
                Base playerBase = null;
                for (Base base :
                        baseList) {
                    if (base.name.equals(player.getScoreboard().getPlayerTeam(player).getName())) {
                        playerBase = base;
                        break;
                    }
                }
                if (playerBase != null) {
                    if (player.getLocation().distance(playerBase.loc) <= 32) {
                        player.openInventory(new BaseInventory().getInventory());
                    } else
                        player.sendMessage(parameters.getPrefix() + ChatColor.RED + "You are too far from your base (>32 blocks)");
                } else
                    player.sendMessage(parameters.getPrefix() + ChatColor.RED + "Your team hasn't placed the flag");
            }
        }

        return true;
    }
}
